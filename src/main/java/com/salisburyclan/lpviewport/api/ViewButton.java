package com.salisburyclan.lpviewport.api;

// ViewButton is a 1-button Viewport.
public interface ViewButton {
  void setLight(Color color);

  // Adds a listener for this viewbutton.
  void addListener(ViewButtonListener listener);

  // Removes a listener from this viewbutton.
  void removeListener(ViewButtonListener listener);
}
