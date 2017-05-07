package com.salisburyclan.launchpad.api;

// ViewStrip is a 1-dimensional Viewport.
public interface ViewStrip {
  // Returns the extent of buttons within this ViewStrip.
  ViewStripExtent getExtent();

  void setLight(int x, Color color);

  // Adds a listener for this viewstrip.
  void addListener(ViewStripListener listener);

  // Returns a new ViewButton relative to this viewstrip.
  ViewButton getSubViewButton(int x);
}
