package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// Interface for writing to a layer.
public interface WriteLayer {
  Range2 getExtent();

  default void setPixel(int x, int y, Color color) {
    setPixel(x, y, Pixel.create(color));
  }

  default void setPixel(Point p, Color color) {
    setPixel(p.x(), p.y(), color);
  }

  void setPixel(int x, int y, Pixel pixel);

  default void setPixel(Point p, Pixel pixel) {
    setPixel(p.x(), p.y(), pixel);
  }

  void close();
}
