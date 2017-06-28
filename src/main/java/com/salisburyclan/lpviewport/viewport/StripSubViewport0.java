package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;

// A viewport that represents a one-button view of an existing viewport.
public class StripSubViewport0 implements Viewport0 {
  private Viewport1 baseViewStrip;
  private int x;

  public StripSubViewport0(Viewport1 baseViewStrip, int x) {
    baseViewStrip.getExtent().assertPointWithin(x);
    this.baseViewStrip = baseViewStrip;
    this.x = x;
  }

  @Override
  public void setPixel(Pixel pixel) {
    baseViewStrip.setPixel(x, pixel);
  }

  @Override
  public void addListener(Button0Listener listener) {
    baseViewStrip.addListener(
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
        });
  }

  @Override
  public void removeListener(Button0Listener listener) {
    // TODO
  }
}
