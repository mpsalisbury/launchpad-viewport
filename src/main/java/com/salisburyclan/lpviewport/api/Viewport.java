package com.salisburyclan.lpviewport.api;

// Viewport is a rectangular set of buttons/lights.
public interface Viewport {
  // Returns the extent of buttons within this Viewport.
  ViewExtent getExtent();

  void setLight(int x, int y, Color color);

  // Sets grid of lights in this viewport.
  // Any pixels outside of this buffer will be ignored.
  // @param x left-most location to start drawing pixels at.
  // @param y top-most location to start drawing pixels at.
  // @param pixels is a list of colors, one per pixel, in Left-to-Right, Top-to-Bottom order.
  //  void setLights(int x, int y, ViewportBuffer pixels);

  // Sets all lights in this viewport to a single color.
  void setAllLights(Color color);

  // Adds a listener for the button at the given position.
  //  void addListener(int x, int y, ButtonListener listener);

  // Adds a listener for this viewport.
  void addListener(ViewportListener listener);

  void removeListener(ViewportListener listener);
}
