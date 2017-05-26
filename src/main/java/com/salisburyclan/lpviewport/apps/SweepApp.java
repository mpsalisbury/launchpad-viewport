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
    new Sweep(viewport, Color.RED, true).play();
  }
}
