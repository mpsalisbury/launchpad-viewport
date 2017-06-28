package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.SubView;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;

public class AllBlue extends LaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.viewport = viewport;
    addStripListener(0, Color.GREEN, Color.BLUE);
    addStripListener(1, Color.YELLOW, Color.BLUE);
    addStripListener(2, Color.RED, Color.BLUE);
    addStripListener(3, Color.ORANGE, Color.BLUE);
    // TODO: fix extentlistener
    addExtentListener(4, 7, Color.PURPLE);
    addButtonListener(6, 4, Color.GREEN);
    addButtonStripListener(5, 5, Color.GREEN);
  }

  private void addButtonListener(int x, int y, Color color) {
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
            button.setPixel(color);
          }
        });
  }

  private void addButtonStripListener(int x, int y, Color color) {
    Range2 extent = viewport.getExtent();
    Viewport1 rowViewStrip = SubView.getSubViewport1(viewport, Range2.create(extent.xRange(), y));
    Viewport0 button = SubView.getSubViewport0(rowViewStrip, x);
    button.addListener(
        new Button0Listener() {
          @Override
          public void onButtonPressed() {
            System.out.println(String.format("ButtonPressed(%s, %s)", x, y));
          }

          public void onButtonReleased() {
            System.out.println(String.format("ButtonReleased(%s, %s)", x, y));
            button.setPixel(color);
          }
        });
  }

  // Handles single row presses.
  private void addStripListener(int row, Color colorOn, Color colorOff) {
    Range2 extent = viewport.getExtent();
    Viewport1 rowViewStrip = SubView.getSubViewport1(viewport, Range2.create(extent.xRange(), row));
    rowViewStrip.addListener(
        new Button1Listener() {
          @Override
          public void onButtonPressed(int x) {
            System.out.println(String.format("StripPressed(%s) row %s", x, row));
            rowViewStrip.setPixel(x, colorOn);
          }

          public void onButtonReleased(int x) {
            System.out.println(String.format("StripReleased(%s) row %s", x, row));
            rowViewStrip.setPixel(x, colorOff);
          }
        });
  }

  private void addExtentListener(int row1, int row2, Color color) {
    Range2 extent = viewport.getExtent();
    Viewport rowViewport =
        SubView.getSubViewport(viewport, Range2.create(extent.xRange(), Range1.create(row1, row2)));
    WriteLayer rowWriteLayer = rowViewport.addLayer();
    rowViewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            System.out.println(String.format("ExtentPressed(%s) row %s", p, row1));
            rowWriteLayer.setPixel(p, color);
          }

          public void onButtonReleased(Point p) {}
        });
  }
}
