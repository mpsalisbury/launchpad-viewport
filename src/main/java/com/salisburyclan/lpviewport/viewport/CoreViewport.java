package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;

public interface CoreViewport {
  ViewExtent getExtent();
  void setLight(int x, int y, Color color);
  void addListener(ViewportListener viewportListener);
  void removeListener(ViewportListener viewportListener);
}
