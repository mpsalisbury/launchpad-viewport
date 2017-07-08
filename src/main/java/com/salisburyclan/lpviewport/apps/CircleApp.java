package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;

public class CircleApp extends LaunchpadApplication {

  private WriteLayer outputLayer;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.outputLayer = viewport.addLayer();
    viewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            Circle.drawRangeCircle(outputLayer, p, 5, 17, Color.CYAN);
          }

          @Override
          public void onButtonReleased(Point p) {
            Circle.drawRangeCircle(outputLayer, p, 5, 17, Color.BLACK);
          }
        });
  }
}
