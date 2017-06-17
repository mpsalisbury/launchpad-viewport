package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.geom.Point;

// A viewport that represents a one-button view of an existing viewport.
public class SubViewButton implements Viewport0 {
  private RawViewport baseViewport;
  private LightLayer baseLightLayer;
  private Point p;

  public SubViewButton(RawViewport baseViewport, Point p) {
    baseViewport.getExtent().assertPointWithin(p);
    this.baseViewport = baseViewport;
    this.baseLightLayer = baseViewport.getLightLayer();
    this.p = p;
  }

  @Override
  public void setLight(Color color) {
    baseLightLayer.setLight(p, color);
  }

  @Override
  public void addListener(Button0Listener listener) {
    baseViewport.addListener(
        new Button2Listener() {
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
  public void removeListener(Button0Listener listener) {
    // TODO
  }
}
