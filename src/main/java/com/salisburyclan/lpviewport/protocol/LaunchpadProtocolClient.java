package com.salisburyclan.lpviewport.protocol;

import com.salisburyclan.lpviewport.geom.Range2;

public interface LaunchpadProtocolClient {
  Range2 getOverallExtent();

  Range2 getPadsExtent();

  void setLight(int pos, int color);
  //  void setLights(int extent, int[] colors);
  //  void setLights(long[] poscolors);
  void setLights(Range2 extent, int color);
  //  void clearLights();
}
