package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;
import java.util.HashMap;
import java.util.Map;

// A viewport that represents a one-button view of an existing viewport.
public class StripSubViewport0 implements Viewport0 {
  private Viewport1 baseViewStrip;
  private int x;
  // Keep track of derived listeners so we can remove them
  // from baseViewport upon request.
  private Map<Button0Listener, Button1Listener> listenerMap;

  public StripSubViewport0(Viewport1 baseViewStrip, int x) {
    baseViewStrip.getExtent().assertPointWithin(x);
    this.baseViewStrip = baseViewStrip;
    this.x = x;
    this.listenerMap = new HashMap<>();
  }

  @Override
  public void setPixel(Pixel pixel) {
    baseViewStrip.setPixel(x, pixel);
  }

  @Override
  public void addListener(Button0Listener listener) {
    Button1Listener subListener =
        new Button1Listener() {
          public void onButtonPressed(int buttonX) {
            if (buttonX == x) {
              listener.onButtonPressed();
            }
          }

          public void onButtonReleased(int buttonX) {
            if (buttonX == x) {
              listener.onButtonReleased();
            }
          }
        };
    listenerMap.put(listener, subListener);
    baseViewStrip.addListener(subListener);
  }

  @Override
  public void removeListener(Button0Listener listener) {
    Button1Listener subListener = listenerMap.remove(listener);
    if (subListener != null) {
      baseViewStrip.removeListener(subListener);
    }
  }

  @Override
  public void removeAllListeners() {
    listenerMap.values().forEach(subListener -> baseViewStrip.removeListener(subListener));
    listenerMap.clear();
  }
}
