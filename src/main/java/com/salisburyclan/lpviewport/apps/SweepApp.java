package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.layer.DColor;

public class SweepApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    Sweep sweep = new Sweep(viewport.getExtent(), DColor.RED, true);
    viewport.addLayer(sweep);
    sweep.play();
  }
}
