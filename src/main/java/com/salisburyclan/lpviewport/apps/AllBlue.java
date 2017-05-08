package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.api.ViewStripListener;

public class AllBlue extends JavafxLaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    viewport = getLaunchpadClient().getViewport();
    addStripListener(0, Color.GREEN, Color.BLUE);
    addStripListener(1, Color.YELLOW, Color.BLUE);
    addStripListener(2, Color.RED, Color.BLUE);
    addStripListener(3, Color.ORANGE, Color.BLUE);
    addExtentListener(4, 7, Color.PURPLE);
    addButtonListener(6, 4, Color.GREEN);
    addButtonStripListener(5, 5, Color.GREEN);
  }

  private void addButtonListener(int x, int y, Color color) {
    ViewButton button = viewport.getSubViewButton(x, y);
    button.addListener(new ViewButtonListener() {
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

  private void addButtonStripListener(int x, int y, Color color) {
    ViewExtent extent = viewport.getExtent();
    ViewStrip rowViewStrip = viewport.getSubViewStrip(
        new ViewExtent(extent.getXLow(), y, extent.getXHigh(), y));
    ViewButton button = rowViewStrip.getSubViewButton(x);
    button.addListener(new ViewButtonListener() {
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

  private void addStripListener(int row, Color colorOn, Color colorOff) {
    ViewExtent extent = viewport.getExtent();
    ViewStrip rowViewStrip = viewport.getSubViewStrip(
        new ViewExtent(extent.getXLow(), row, extent.getXHigh(), row));
    rowViewStrip.addListener(new ViewStripListener() {
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

  private void addExtentListener(int row1, int row2, Color color) {
    ViewExtent extent = viewport.getExtent();
    Viewport rowViewport = viewport.getSubViewport(
        new ViewExtent(extent.getXLow(), row1, extent.getXHigh(), row2));
    rowViewport.addListener(new ViewportListener() {
      @Override
      public void onButtonPressed(int x, int y) {
        System.out.println(String.format("ExtentPressed(%s, %s) row %s", x, y, row1));
        rowViewport.setLight(x, y, color);
      }
      public void onButtonReleased(int x, int y) {
      }
    });
  }
}
