package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.SubView;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;

public class AllBlue extends JavafxLaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.viewport = viewport;
    addStripListener(0, DColor.GREEN, DColor.BLUE);
    addStripListener(1, DColor.YELLOW, DColor.BLUE);
    addStripListener(2, DColor.RED, DColor.BLUE);
    addStripListener(3, DColor.ORANGE, DColor.BLUE);
    addExtentListener(4, 7, DColor.PURPLE);
    addButtonListener(6, 4, DColor.GREEN);
    addButtonStripListener(5, 5, DColor.GREEN);
  }

  private void addButtonListener(int x, int y, DColor color) {
    Point p = Point.create(x, y);
    Viewport0 button = SubView.getSubViewport0(viewport, p);
    button.addListener(
        new Button0Listener() {
          @Override
          public void onButtonPressed() {
            System.out.println(String.format("ButtonPressed(%s)", p));
          }

          public void onButtonReleased() {
            System.out.println(String.format("ButtonReleased(%s)", p));
            button.setLight(color);
          }
        });
  }

  private void addButtonStripListener(int x, int y, DColor color) {
    Range2 extent = viewport.getExtent();
    Viewport1 rowViewStrip = SubView.getSubViewStrip(viewport, Range2.create(extent.xRange(), y));
    Viewport0 button = SubView.getSubViewport0(rowViewStrip, x);
    button.addListener(
        new Button0Listener() {
          @Override
          public void onButtonPressed() {
            System.out.println(String.format("ButtonPressed(%s, %s)", x, y));
          }

          public void onButtonReleased() {
            System.out.println(String.format("ButtonReleased(%s, %s)", x, y));
            button.setLight(color);
          }
        });
  }

  private void addStripListener(int row, DColor colorOn, DColor colorOff) {
    Range2 extent = viewport.getExtent();
    Viewport1 rowViewStrip = SubView.getSubViewStrip(viewport, Range2.create(extent.xRange(), row));
    rowViewStrip.addListener(
        new Button1Listener() {
          @Override
          public void onButtonPressed(int x) {
            System.out.println(String.format("StripPressed(%s) row %s", x, row));
            rowViewStrip.setLight(x, colorOn);
          }

          public void onButtonReleased(int x) {
            System.out.println(String.format("StripReleased(%s) row %s", x, row));
            rowViewStrip.setLight(x, colorOff);
          }
        });
  }

  private void addExtentListener(int row1, int row2, DColor color) {
    Range2 extent = viewport.getExtent();
    Viewport rowViewport =
        SubView.getSubViewport(viewport, Range2.create(extent.xRange(), Range1.create(row1, row2)));
    rowViewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            System.out.println(String.format("ExtentPressed(%s) row %s", p, row1));
            rowViewport.getLightLayer().setLight(p, color);
          }

          public void onButtonReleased(Point p) {}
        });
  }
}
