package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.layer.LayerBuffer;

public class SweepApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getLayer(this::setupLayer);
  }

  private void setupLayer(LayerBuffer layer) {
    new Sweep(layer, Color.RED, true).play();
  }
}
