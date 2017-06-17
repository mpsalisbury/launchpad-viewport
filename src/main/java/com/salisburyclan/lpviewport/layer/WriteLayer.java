package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// Interface for writing to a layer.
public interface WriteLayer {
  Range2 getExtent();

  void setPixel(int x, int y, DColor color);

  default void setPixel(Point p, DColor color) {
    setPixel(p.x(), p.y(), color);
  }

  void setPixel(int x, int y, Pixel pixel);

  default void setPixel(Point p, Pixel pixel) {
    setPixel(p.x(), p.y(), pixel);
  }

  void close();
}
