package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.api.ViewStripExtent;
import com.salisburyclan.lpviewport.api.ViewStripListener;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewStrip implements ViewStrip {
  private Viewport baseViewport;
  private ViewStripExtent extent;
  private IndexMap indexMap;

  public SubViewStrip(Viewport baseViewport, ViewExtent extent) {
    this.baseViewport = baseViewport;
    this.indexMap = newIndexMap(extent);
  }

  private IndexMap newIndexMap(ViewExtent extent) {
    checkExtent(extent);
    if (extent.getWidth() == 1) {
      return new VerticalIndexMap(extent);
    }
    if (extent.getHeight() == 1) {
      return new HorizontalIndexMap(extent);
    }
    throw new IllegalArgumentException("ViewStrip extent must be only one button wide or high");
  }

  private void checkExtent(ViewExtent extent) {
    if (!baseViewport.getExtent().isExtentWithin(extent)) {
      throw new IllegalArgumentException("Extent extends beyond base viewport: " + extent.toString());
    }
  }

  @Override
  public ViewStripExtent getExtent() {
    return indexMap.getExtent();
  }

  @Override
  public void setLight(int index, Color color) {
    baseViewport.setLight(indexMap.getX(index), indexMap.getY(index), color);
  }

  @Override
  public void addListener(ViewStripListener listener) {
    baseViewport.addListener(new ViewportListener() {
      public void onButtonPressed(int x, int y) {
        if (indexMap.isPointWithin(x, y)) {
          listener.onButtonPressed(indexMap.getIndex(x, y));
        }
      }
      public void onButtonReleased(int x, int y) {
        if (indexMap.isPointWithin(x, y)) {
          listener.onButtonReleased(indexMap.getIndex(x, y));
        }
      }
    });
  }
  @Override
  public void removeListener(ViewStripListener listener) {
	  // TODO implement
    throw new UnsupportedOperationException("SubViewStrip::removeListener");
  }

  // Maps between 1-D strip index and 2-D Viewport index.
  private interface IndexMap {
    int getX(int index);
    int getY(int index);
    boolean isPointWithin(int x, int y);
    int getIndex(int x, int y);
    ViewStripExtent getExtent();
  }

  private static class HorizontalIndexMap implements IndexMap {
    private int xLow;
    private int xHigh;
    private int yVal;
    private ViewStripExtent extent;

    public HorizontalIndexMap(ViewExtent extent) {
      xLow = extent.getXLow();
      xHigh = extent.getXHigh();
      yVal = extent.getYLow();
      this.extent = new ViewStripExtent(xLow, xHigh);
    }

    @Override
    public int getX(int index) {
      return xLow + index;
    }

    @Override
    public int getY(int index) {
      return yVal;
    }

    @Override
    public boolean isPointWithin(int x, int y) {
      return (x >= xLow) && (x <= xHigh) && (y == yVal);
    }

    @Override
    public int getIndex(int x, int y) {
      return x - xLow;
    }

    @Override
    public ViewStripExtent getExtent() {
      return extent;
    }
  }

  private static class VerticalIndexMap implements IndexMap {
    private int xVal;
    private int yLow;
    private int yHigh;
    private ViewStripExtent extent;

    public VerticalIndexMap(ViewExtent extent) {
      xVal = extent.getXLow();
      yLow = extent.getYLow();
      yHigh = extent.getYHigh();
      this.extent = new ViewStripExtent(yLow, yHigh);
    }

    @Override
    public int getX(int index) {
      return xVal;
    }

    @Override
    public int getY(int index) {
      return yLow + index;
    }

    @Override
    public boolean isPointWithin(int x, int y) {
      return (y >= yLow) && (y <= yHigh) && (x == xVal);
    }

    @Override
    public int getIndex(int x, int y) {
      return y - yLow;
    }

    @Override
    public ViewStripExtent getExtent() {
      return extent;
    }
  }
}

