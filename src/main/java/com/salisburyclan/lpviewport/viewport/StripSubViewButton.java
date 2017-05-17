package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.api.ViewStripListener;

// A viewport that represents a one-button view of an existing viewport.
public class StripSubViewButton implements ViewButton {
  private ViewStrip baseViewStrip;
  private int x;

  public StripSubViewButton(ViewStrip baseViewStrip, int x) {
    baseViewStrip.getExtent().assertPointWithin(x);
    this.baseViewStrip = baseViewStrip;
    this.x = x;
  }

  @Override
  public void setLight(Color color) {
    baseViewStrip.setLight(x, color);
  }

  @Override
  public void addListener(ViewButtonListener listener) {
    baseViewStrip.addListener(new ViewStripListener() {
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
}
