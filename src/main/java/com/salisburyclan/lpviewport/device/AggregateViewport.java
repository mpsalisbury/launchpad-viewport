package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;

// A viewport that represents multiple viewports stitched together
public class AggregateViewport implements Viewport {
  // for now, just left-to-right
  private Viewport leftViewport;
  private Viewport rightViewport;

  private ViewExtent leftExtent;
  private ViewExtent rightExtent;
  // offset of rightViewport from origin.
  private int offset;
  private ViewExtent extent;

  public AggregateViewport(Viewport leftViewport, Viewport rightViewport) {
    this.leftViewport = leftViewport;
    this.rightViewport = rightViewport;

    // for now assume viewports have same dimensions
    this.leftExtent = leftViewport.getExtent();
    this.offset = leftExtent.getWidth();
    this.extent = new ViewExtent(0, 0, leftExtent.getWidth() * 2 - 1, leftExtent.getHeight() - 1);
    this.rightExtent = new ViewExtent(offset, 0, leftExtent.getWidth() * 2 - 1, leftExtent.getHeight() - 1);
  }

  @Override
  public ViewExtent getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    if (leftExtent.isPointWithin(x, y)) {
      leftViewport.setLight(x, y, color);
    } else if (rightExtent.isPointWithin(x, y)) {
      rightViewport.setLight(x - offset, y, color);
    }
  }

  @Override
  public void addListener(ViewportListener listener) {
    leftViewport.addListener(new ViewportListener() {
      public void onButtonPressed(int x, int y) {
        listener.onButtonPressed(x, y);
      }
      public void onButtonReleased(int x, int y) {
        listener.onButtonReleased(x, y);
      }
    });
    rightViewport.addListener(new ViewportListener() {
      public void onButtonPressed(int x, int y) {
        listener.onButtonPressed(x + offset, y);
      }
      public void onButtonReleased(int x, int y) {
        listener.onButtonReleased(x + offset, y);
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
    return new SubViewport.SubViewButton(this, x, y);
  }
}

