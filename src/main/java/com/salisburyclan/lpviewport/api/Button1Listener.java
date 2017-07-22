package com.salisburyclan.lpviewport.api;

// Reports when a button in a Viewport1 has been pressed or released.
// Indices match the values in the extent of the Viewport1.
public interface Button1Listener {

  // Button at position p was pressed.
  void onButtonPressed(int p);

  // Button at position p was released.
  default void onButtonReleased(int p) {}
}
