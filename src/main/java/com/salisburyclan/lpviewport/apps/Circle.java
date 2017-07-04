package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.WriteLayer;

public class Circle extends LaunchpadApplication {

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
            drawCircle(p, 10, Color.CYAN);
          }

          @Override
          public void onButtonReleased(Point p) {
            drawCircle(p, 10, Color.BLACK);
          }
        });
  }

  private void drawCircle(Point center, int radius, Color color) {
    int x = 0;
    int y = radius;
    int e = 0;
    while (y >= x) {
      drawEight(center, x, y, color);
      e += 2 * x + 1;
      if (2 * e + (1 - 2 * y) <= 0) {
        x++;
      } else {
        e += (1 - 2 * y);
        x++;
        y--;
      }
    }
  }

  private void drawEight(Point center, int x, int y, Color color) {
    outputLayer.setPixel(center.x() + x, center.y() + y, color);
    outputLayer.setPixel(center.x() + y, center.y() + x, color);
    outputLayer.setPixel(center.x() + y, center.y() - x, color);
    outputLayer.setPixel(center.x() + x, center.y() - y, color);
    outputLayer.setPixel(center.x() - x, center.y() - y, color);
    outputLayer.setPixel(center.x() - y, center.y() - x, color);
    outputLayer.setPixel(center.x() - y, center.y() + x, color);
    outputLayer.setPixel(center.x() - x, center.y() + y, color);
  }
}
