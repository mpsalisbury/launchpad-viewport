package com.salisburyclan.lpviewport.api;

// Reports when a button has been pressed or released.
public interface Button0Listener {

	/** Fired when the button was pressed. */
  void onButtonPressed();

	/** Fired when the button was released. */
  default void onButtonReleased() {}
}
