package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import java.util.HashMap;
import java.util.Map;

// A viewport that represents a one-button view of an existing viewport.
public class SubViewport0 implements Viewport0 {
  private Viewport baseViewport;
  private WriteLayer baseWriteLayer;
  private Point p;
  // Keep track of derived listeners so we can remove them
  // from baseViewport upon request.
  private Map<Button0Listener, Button2Listener> listenerMap;

  public SubViewport0(Viewport baseViewport, Point p) {
    baseViewport.getExtent().assertPointWithin(p);
    this.baseViewport = baseViewport;
    this.baseWriteLayer = baseViewport.addLayer();
    this.p = p;
    this.listenerMap = new HashMap<>();
  }

  @Override
  public void setPixel(Pixel pixel) {
    baseWriteLayer.setPixel(p, pixel);
  }

  @Override
  public void addListener(Button0Listener listener) {
    Button2Listener subListener =
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
        };
    listenerMap.put(listener, subListener);
    baseViewport.addListener(subListener);
  }

  @Override
  public void removeListener(Button0Listener listener) {
    Button2Listener subListener = listenerMap.remove(listener);
    if (subListener != null) {
      baseViewport.removeListener(subListener);
    }
  }
}
