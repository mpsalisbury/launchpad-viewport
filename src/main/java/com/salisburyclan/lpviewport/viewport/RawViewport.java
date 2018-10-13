package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.geom.Range2;

// RawViewport is a rectangular set of buttons/lights.
public interface RawViewport {
  // Returns the extent of buttons within this Viewport.
  Range2 getExtent();

  RawLayer getRawLayer();

  // Adds a listener for this viewport.
  void addListener(Button2Listener listener);

  void removeListener(Button2Listener listener);

  void removeAllListeners();
}
