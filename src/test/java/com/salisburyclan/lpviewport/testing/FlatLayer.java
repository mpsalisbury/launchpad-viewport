package com.salisburyclan.lpviewport.testing;

import com.salisburyclan.lpviewport.api.CloseListener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CloseListenerMultiplexer;

// A layer containing just a single pixel value throughout its extent.
public class FlatLayer implements ReadLayer {
  private Range2 extent;
  private Pixel pixel;
  private CloseListenerMultiplexer closeListeners;

  public FlatLayer(Range2 extent, Pixel pixel) {
    this.extent = extent;
    this.pixel = pixel;
    this.closeListeners = new CloseListenerMultiplexer();
  }

  public Range2 getExtent() {
    return extent;
  }

  public Pixel getPixel(int x, int y) {
    if (extent.isPointWithin(x, y)) {
      return pixel;
    } else {
      return Pixel.EMPTY;
    }
  }

  public void close() {
    closeListeners.onClose();
  }

  public void addPixelListener(PixelListener listener) {
    // Pixels don't change. Do nothing.
  }

  public void removePixelListener(PixelListener listener) {
    // Pixels don't change. Do nothing.
  }

  public void removeAllPixelListeners() {
    // Pixels don't change. Do nothing.
  }

  public void addCloseListener(CloseListener listener) {
    closeListeners.add(listener);
  }
}
