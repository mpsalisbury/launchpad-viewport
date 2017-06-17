package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;

public class SweepApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    Sweep sweep = new Sweep(viewport.getExtent(), Color.RED, true);
    viewport.addLayer(sweep);
    sweep.play();
  }
}
