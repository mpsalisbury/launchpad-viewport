package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// A rectangular set of settable pixels.
public interface RawLayer {
  // Returns the extent of pixels within this layer.
  Range2 getExtent();

  void setPixel(int x, int y, Color color);

  default void setPixel(Point p, Color color) {
    setPixel(p.x(), p.y(), color);
  }

  // Sets all pixels in this layer to the given color.
  void setAllPixels(Color color);
}
