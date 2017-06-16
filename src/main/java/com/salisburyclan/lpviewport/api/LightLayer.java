package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// A rectangular set of settable lights.
public interface LightLayer {
  // Returns the extent of lights within this layer.
  Range2 getExtent();

  void setLight(int x, int y, Color color);

  default void setLight(Point p, Color color) {
    setLight(p.x(), p.y(), color);
  }

  // Sets all lights in this layer to the given color.
  void setAllLights(Color color);
}
