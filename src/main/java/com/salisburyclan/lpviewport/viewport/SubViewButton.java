package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.WriteLayer;

// A viewport that represents a one-button view of an existing viewport.
public class SubViewButton implements Viewport0 {
  private Viewport baseViewport;
  private WriteLayer baseWriteLayer;
  private Point p;

  public SubViewButton(Viewport baseViewport, Point p) {
    baseViewport.getExtent().assertPointWithin(p);
    this.baseViewport = baseViewport;
    this.baseWriteLayer = baseViewport.addLayer();
    this.p = p;
  }

  @Override
  public void setPixel(Pixel pixel) {
    baseWriteLayer.setPixel(p, pixel);
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
