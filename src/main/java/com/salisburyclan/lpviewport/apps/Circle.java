package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.WriteLayer;

public class Circle extends JavafxLaunchpadApplication {

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
            drawCircle(p, 10, DColor.create(Color.CYAN));
          }

          @Override
          public void onButtonReleased(Point p) {
            drawCircle(p, 10, DColor.BLACK);
          }
        });
  }

  // private void drawOldCircle(Point center, int radius, DColor color) {
  //   for (int degrees = 0; degrees < 360; degrees++) {
  //     double radians = degrees * (Math.PI / 180);
  //     int x = center.x() + (int) Math.round(Math.sin(radians) * radius);
  //     int y = center.y() + (int) Math.round(Math.cos(radians) * radius);
  //     outputLayer.setPixel(x, y, color);
  //   }
  // }

  private void drawCircle(Point center, int radius, DColor color) {
    int x = 0;
    int y = radius;
    int e = 0;
    while (y >= x) {
      drawEight(center, x, y, color);
      if (2 * (e + 2 * x + 1) + (1 - 2 * y) <= 0) {
        e += 2 * x + 1;
        x++;
      } else {
        e += (2 * x + 1) + (1 - 2 * y);
        x++;
        y--;
      }
    }
  }

  private void drawEight(Point center, int x, int y, DColor color) {
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
