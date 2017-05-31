package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range1;

// ViewStrip is a 1-dimensional Viewport.
public interface ViewStrip {
  // Returns the extent of buttons within this ViewStrip.
  Range1 getExtent();

  void setLight(int x, Color color);

  // Adds a listener for this viewstrip.
  void addListener(ViewStripListener listener);

  // Removes a listener from this viewstrip.
  void removeListener(ViewStripListener listener);
}
