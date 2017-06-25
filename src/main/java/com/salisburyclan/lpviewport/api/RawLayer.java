package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;

// A rectangular set of settable pixels.
public interface RawLayer {
  // Returns the extent of pixels within this layer.
  Range2 getExtent();

  void setPixel(int x, int y, DColor color);

  default void setPixel(Point p, DColor color) {
    setPixel(p.x(), p.y(), color);
  }

  // Sets all pixels in this layer to the given color.
  void setAllPixels(DColor color);
}
