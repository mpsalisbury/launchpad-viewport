package com.salisburyclan.lpviewport.api;

// Reports when a button in a Viewport1 has been pressed or released.
public interface Button1Listener {
  // Button at position p was pressed.
  void onButtonPressed(int p);

  // Button at position p was released.
  default void onButtonReleased(int p) {}
}
