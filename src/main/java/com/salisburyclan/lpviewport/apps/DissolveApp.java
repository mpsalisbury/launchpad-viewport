package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.RandomFill;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.application.Platform;

public class DissolveApp extends LaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    Range2 extent = viewport.getExtent();
    viewport.addLayer(new Rainbow.RainbowLayer(extent));

    viewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            RandomFill dissolve = new RandomFill(extent);
            dissolve.addAnimationListener(
                () -> {
                  viewport.removeAllLayers();
                  Platform.exit();
                });
            viewport.addLayer(dissolve);
            dissolve.play();
          }
        });
  }
}
