package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// Viewport is a rectangular set of buttons/lights.
public interface Viewport {
  // Returns the extent represented by this Viewport.
  Range2 getExtent();

  // Adds a new layer on top of the existing layers in this Viewport.
  ReadWriteLayer addLayer();

  // Adds the given layer on top of the existing layers in this Viewport.
  void addLayer(ReadLayer layer);

  // Removes the given layer from this Viewport.
  void removeLayer(ReadLayer layer);

  // Adds a button listener to this Viewport.
  void addListener(Button2Listener listener);

  // Removes a button listener from this Viewport.
  void removeListener(Button2Listener listener);

  // Removes all button listeners from this Viewport.
  void removeAllListeners();
}
