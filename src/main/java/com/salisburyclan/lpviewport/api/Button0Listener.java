package com.salisburyclan.lpviewport.api;

// Reports when a button has been pressed or released.
public interface Button0Listener {
  void onButtonPressed();

  default void onButtonReleased() {}
}
