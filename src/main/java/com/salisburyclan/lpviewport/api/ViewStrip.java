package com.salisburyclan.lpviewport.api;

// ViewStrip is a 1-dimensional Viewport.
public interface ViewStrip {
  // Returns the extent of buttons within this ViewStrip.
  ViewStripExtent getExtent();

  void setLight(int x, Color color);

  // Adds a listener for this viewstrip.
  void addListener(ViewStripListener listener);
}
