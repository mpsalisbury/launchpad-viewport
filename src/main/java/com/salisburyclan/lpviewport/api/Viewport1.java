package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range1;

// Viewport1 is a 1-dimensional Viewport.
public interface Viewport1 {
  // Returns the extent of buttons within this ViewStrip.
  Range1 getExtent();

  void setPixel(int p, Pixel pixel);

  default void setPixel(int p, Color color) {
    setPixel(p, Pixel.create(color));
  }

  // Adds a listener for this viewstrip.
  void addListener(Button1Listener listener);

  // Removes a listener from this viewstrip.
  void removeListener(Button1Listener listener);

  // Removes all listeners from this viewstrip.
  void removeAllListeners();
}
