package com.salisburyclan.lpviewport.protocol;

import com.salisburyclan.lpviewport.api.ViewExtent;

public interface LaunchpadProtocolClient {
  ViewExtent getOverallExtent();
  ViewExtent getPadsExtent();

  void setLight(int pos, int color);
//  void setLights(int extent, int[] colors);
//  void setLights(long[] poscolors);
  void setLights(ViewExtent extent, int color);
//  void clearLights();
}
