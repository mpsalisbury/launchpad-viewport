package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.Pixel;

// Viewport0 is a 1-button (0-dimensional) Viewport.
public interface Viewport0 {
  void setPixel(Pixel pixel);
  
  default void setPixel(DColor color) {
    setPixel(Pixel.create(color));
  }

  // Adds a listener for this viewbutton.
  void addListener(Button0Listener listener);

  // Removes a listener from this viewbutton.
  void removeListener(Button0Listener listener);
}
