package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;

// Reports when a button in a Viewport has been pressed or released.
public interface Button2Listener {
  // Button at position p was pressed.
  void onButtonPressed(Point p);

  // Button at position p was released.
  default void onButtonReleased(Point p) {}
}
