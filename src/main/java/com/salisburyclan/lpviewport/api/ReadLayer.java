package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// An image layer holding a set of readable pixels and transparencies.
// Interface for reading from a layer.
public interface ReadLayer {
  // Returns the pixel extent for this layer.
  Range2 getExtent();

  // Reads one pixel in this layer.
  Pixel getPixel(int x, int y);

  default Pixel getPixel(Point p) {
    return getPixel(p.x(), p.y());
  }

  // Adds a listener that is notified when a pixel has changed value.
  void addPixelListener(PixelListener listener);

  void removePixelListener(PixelListener listener);

  void removeAllPixelListeners();

  // Adds a listener that is notified when this layer is closed.
  void addCloseListener(CloseListener listener);
}
