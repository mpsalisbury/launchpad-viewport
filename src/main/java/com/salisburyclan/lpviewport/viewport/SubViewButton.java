package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;

// A viewport that represents a one-button view of an existing viewport.
public class SubViewButton implements ViewButton {
  private Viewport baseViewport;
  private Point p;

  public SubViewButton(Viewport baseViewport, Point p) {
    baseViewport.getExtent().assertPointWithin(p);
    this.baseViewport = baseViewport;
    this.p = p;
  }

  @Override
  public void setLight(Color color) {
    baseViewport.setLight(p, color);
  }

  @Override
  public void addListener(ViewButtonListener listener) {
    baseViewport.addListener(
        new ViewportListener() {
          public void onButtonPressed(Point buttonPoint) {
            if (p.equals(buttonPoint)) {
              listener.onButtonPressed();
            }
          }

          public void onButtonReleased(Point buttonPoint) {
            if (p.equals(buttonPoint)) {
              listener.onButtonReleased();
            }
          }
        });
  }

  @Override
  public void removeListener(ViewButtonListener listener) {
    // TODO
  }
}
