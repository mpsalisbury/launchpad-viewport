package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;

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

  /**
   * TODO: remove
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
  */
}
