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
            drawCircle(p, Color.CYAN, Color.BLUE);
          }

          @Override
          public void onButtonReleased(Point p) {
            drawCircle(p, Color.BLACK, Color.BLACK);
          }
        });
  }

  private void drawCircle(Point center, Color color, Color color2) {
    for (int i = 0; i < 10; i += 2) {
      Circle.drawRangeCircle(outputLayer, center, i, i + 1, color);
      Circle.drawRangeCircle(outputLayer, center, i + 1, i + 2, color2);
    }
  }
}
