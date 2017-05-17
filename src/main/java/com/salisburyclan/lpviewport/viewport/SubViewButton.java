package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;

// A viewport that represents a one-button view of an existing viewport.
public class SubViewButton implements ViewButton {
  private Viewport baseViewport;
  private int x;
  private int y;

  public SubViewButton(Viewport baseViewport, int x, int y) {
    baseViewport.getExtent().assertPointWithin(x, y);
    this.baseViewport = baseViewport;
    this.x = x;
    this.y = y;
  }

  @Override
  public void setLight(Color color) {
    baseViewport.setLight(x, y, color);
  }

  @Override
  public void addListener(ViewButtonListener listener) {
    baseViewport.addListener(new ViewportListener() {
      public void onButtonPressed(int buttonX, int buttonY) {
        if (buttonX == x && buttonY == y) {
          listener.onButtonPressed();
        }
      }
      public void onButtonReleased(int buttonX, int buttonY) {
        if (buttonX == x && buttonY == y) {
          listener.onButtonReleased();
        }
      }
    });
  }
}
