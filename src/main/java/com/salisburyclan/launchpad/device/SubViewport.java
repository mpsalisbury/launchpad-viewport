package com.salisburyclan.launchpad.device;

import com.salisburyclan.launchpad.api.Color;
import com.salisburyclan.launchpad.api.Viewport;
import com.salisburyclan.launchpad.api.ViewButton;
import com.salisburyclan.launchpad.api.ViewButtonListener;
import com.salisburyclan.launchpad.api.ViewExtent;
import com.salisburyclan.launchpad.api.ViewportListener;
import com.salisburyclan.launchpad.api.ViewStrip;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements Viewport {
  private Viewport baseViewport;
  private ViewExtent extent;

  public SubViewport(Viewport baseViewport, ViewExtent extent) {
    this.baseViewport = baseViewport;
    this.extent = extent;
    checkExtent(extent);
  }

  private void checkExtent(ViewExtent extent) {
    // TODO add extent logic to ViewExtent instead.
    // TODO add viewextent to Viewport
    if (!baseViewport.getExtent().isExtentWithin(extent)) {
      throw new IllegalArgumentException("Extent extends beyond base viewport: " + extent.toString());
    }
  }

  @Override
  public ViewExtent getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    baseViewport.setLight(x + extent.getXLow(), y + extent.getYLow(), color);
  }

  @Override
  public void addListener(ViewportListener listener) {
    baseViewport.addListener(new ViewportListener() {
      public void onButtonPressed(int x, int y) {
        if (extent.isPointWithin(x, y)) {
          listener.onButtonPressed(x - extent.getXLow(), y - extent.getYLow());
        }
      }
      public void onButtonReleased(int x, int y) {
        if (extent.isPointWithin(x, y)) {
          listener.onButtonReleased(x - extent.getXLow(), y - extent.getYLow());
        }
      }
    });
  }

  @Override
  public Viewport getSubViewport(ViewExtent extent) {
    return new SubViewport(this, extent);
  }

  @Override
  public ViewStrip getSubViewStrip(ViewExtent extent) {
    return new SubViewStrip(this, extent);
  }

  @Override
  public ViewButton getSubViewButton(int x, int y) {
    return new SubViewButton(this, x, y);
  }

  // A viewport that represents a one-button view of an existing viewport.
  public static class SubViewButton implements ViewButton {
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
}

