package com.salisburyclan.launchpad.protocol;

import com.salisburyclan.launchpad.api.ViewExtent;

public interface LaunchpadProtocolClient {
  ViewExtent getOverallExtent();
  ViewExtent getPadsExtent();

  void setLight(int pos, int color);
//  void setLights(int extent, int[] colors);
//  void setLights(long[] poscolors);
//  void setLights(int extent, int color);
//  void clearLights();
}
