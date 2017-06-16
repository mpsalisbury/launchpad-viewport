package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// A buffer of colored pixels for staging images.
public class LayerBuffer implements Layer {
  private Range2 extent;
  private Pixel[][] buffer;
  private PixelListenerMultiplexer pixelListeners;
  private CloseListener closer;

  public LayerBuffer(Range2 extent) {
    this.extent = extent;
    this.buffer = new Pixel[extent.getWidth()][extent.getHeight()];
    this.pixelListeners = new PixelListenerMultiplexer();
    setAllPixels(Pixel.EMPTY);
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.addListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener closer) {
    this.closer = closer;
  }

  // Remove this buffer from container.
  public void close() {
    if (closer != null) {
      closer.onClose();
    }
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

  public void setPixel(int x, int y, DColor color) {
    setPixel(x, y, Pixel.create(color));
  }

  public void setPixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      Point origin = extent.origin();
      buffer[x - origin.x()][y - origin.y()] = pixel;
      pixelListeners.onSetPixel(x, y);
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
