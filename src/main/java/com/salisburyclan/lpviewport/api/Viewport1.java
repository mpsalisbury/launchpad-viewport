package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range1;

// Viewport1 is a 1-dimensional Viewport.
public interface Viewport1 {
  // Returns the extent of buttons within this ViewStrip.
  Range1 getExtent();

  void setLight(int p, Color color);

  // Adds a listener for this viewstrip.
  void addListener(Button1Listener listener);

  // Removes a listener from this viewstrip.
  void removeListener(Button1Listener listener);
}
