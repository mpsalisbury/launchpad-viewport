package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// A buffer of colored pixels for staging images.
public class LayerBuffer implements Layer, FrameWriteLayer {
  private Range2 extent;
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
    pixelListeners.addListener(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.removeListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener closer) {
    closeListeners.addListener(closer);
  }

  @Override
  public void nextFrame() {
    pixelListeners.onNextFrame();
    setAllPixels(Pixel.EMPTY);
  }

  // Remove this buffer from container.
  @Override
  public void close() {
    setAllPixels(Pixel.EMPTY);
    closeListeners.onClose();
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
      return Pixel.EMPTY; // TODO error instead?
    }
  }

  @Override
  public void setPixel(int x, int y, DColor color) {
    setPixel(x, y, Pixel.create(color));
  }

  @Override
  public void setPixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      Point origin = extent.origin();
      Pixel oldPixel = buffer[x - origin.x()][y - origin.y()];
      if (!pixel.equals(oldPixel)) {
        buffer[x - origin.x()][y - origin.y()] = pixel;
        pixelListeners.onSetPixel(x, y);
      }
    }
  }

  // Place given pixel on top of existing pixel at location (x,y).
  public void combinePixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      setPixel(x, y, getPixel(x, y).combine(pixel));
    }
  }

  private void setAllPixels(Pixel pixel) {
    extent.forEach((x, y) -> setPixel(x, y, pixel));
  }
}
