package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// RawViewport is a rectangular set of buttons/lights.
public interface RawViewport {
  // Returns the extent of buttons within this Viewport.
  Range2 getExtent();

  LightLayer getLightLayer();

  // Adds a listener for this viewport.
  void addListener(Button2Listener listener);

  void removeListener(Button2Listener listener);
}
