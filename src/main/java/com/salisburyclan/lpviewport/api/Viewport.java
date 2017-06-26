package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// Viewport is a rectangular set of buttons/lights.
public interface Viewport {
  Range2 getExtent();

  LayerBuffer addLayer();

  void addLayer(ReadLayer layer);

  void removeLayer(ReadLayer layer);

  void addListener(Button2Listener listener);

  void removeListener(Button2Listener listener);
}
