package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;

// TODO(mpsalisbury) devices should use this instead of Viewport
public interface DeviceViewport {
  ViewExtent getExtent();
  void setLight(int x, int y, Color color);
  void addListener(ViewportListener viewportListener);
}
