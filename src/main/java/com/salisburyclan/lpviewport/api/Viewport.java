package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.LayerBuffer;

// Viewport is a rectangular set of buttons/lights.
public interface Viewport {
  Range2 getExtent();

  LayerBuffer addLayer();

  void addLayer(Layer layer);

  void addListener(Button2Listener listener);

  void removeListener(Button2Listener listener);
}
