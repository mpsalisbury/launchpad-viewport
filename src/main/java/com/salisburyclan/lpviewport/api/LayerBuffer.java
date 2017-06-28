package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CloseListenerMultiplexer;
import com.salisburyclan.lpviewport.util.PixelListenerMultiplexer;

// A buffer of pixels for staging images.
public class LayerBuffer implements ReadLayer, WriteLayer {
  // The 2d extent of this buffer.
  private Range2 extent;
  // The pixels[x][y] in this buffer. Indices are 0-based.
  private Pixel[][] buffer;
  private PixelListenerMultiplexer pixelListeners;
  private CloseListenerMultiplexer closeListeners;

  public LayerBuffer(Range2 extent) {
    this.extent = extent;
    this.buffer = new Pixel[extent.getWidth()][extent.getHeight()];
    this.pixelListeners = new PixelListenerMultiplexer();
    this.closeListeners = new CloseListenerMultiplexer();
    setAllPixels(Pixel.EMPTY);
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.add(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.remove(listener);
  }

  @Override
  public void addCloseListener(CloseListener closer) {
    closeListeners.add(closer);
  }

  @Override
  public void nextFrame() {
    pixelListeners.onNextFrame();
    setAllPixels(Pixel.EMPTY);
  }

  @Override
  public void close() {
    setAllPixels(Pixel.EMPTY);
    closeListeners.onClose();
    pixelListeners.clear();
    closeListeners.clear();
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    if (extent.isPointWithin(x, y)) {
      Point origin = extent.origin();
      return buffer[x - origin.x()][y - origin.y()];
    } else {
      return Pixel.EMPTY;
    }
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    setPixel(x, y, Pixel.create(color));
  }

  @Override
  public void setPixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      Point origin = extent.origin();
      Pixel oldPixel = buffer[x - origin.x()][y - origin.y()];
      if (!pixel.equals(oldPixel)) {
        buffer[x - origin.x()][y - origin.y()] = pixel;
        pixelListeners.onPixelChanged(Point.create(x, y));
      }
    }
  }

  // Place given pixel on top of existing pixel at location (x,y).
  public void combinePixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      setPixel(x, y, getPixel(x, y).combine(pixel));
    }
  }

  // Sets all pixels of this buffer to given pixel value.
  private void setAllPixels(Pixel pixel) {
    extent.forEach((x, y) -> setPixel(x, y, pixel));
  }
}
