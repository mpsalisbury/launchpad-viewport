package com.salisburyclan.lpviewport.api;

// Viewport0 is a 1-button (0-dimensional) Viewport.
public interface Viewport0 {
  void setPixel(Pixel pixel);

  default void setPixel(Color color) {
    setPixel(Pixel.create(color));
  }

  // Adds a listener for this viewbutton.
  void addListener(Button0Listener listener);

  // Removes a listener from this viewbutton.
  void removeListener(Button0Listener listener);
}
