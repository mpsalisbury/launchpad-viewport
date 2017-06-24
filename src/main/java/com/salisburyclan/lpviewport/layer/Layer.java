package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// An image layer holding a set of readable pixels and transparencies.
// Interface for reading from a layer.
public interface Layer {
  // Returns the pixel extent for this layer.
  Range2 getExtent();

  // Reads one pixel in this layer.
  Pixel getPixel(int x, int y);

  default Pixel getPixel(Point p) {
    return getPixel(p.x(), p.y());
  }

  void addPixelListener(PixelListener listener);

  void removePixelListener(PixelListener listener);

  void addCloseListener(CloseListener listener);
}
