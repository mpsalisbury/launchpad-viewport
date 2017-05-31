package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.api.ViewStripListener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewStrip implements ViewStrip {
  private Viewport baseViewport;
  private Range1 extent;
  private IndexMap indexMap;

  public SubViewStrip(Viewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.indexMap = newIndexMap(extent);
  }

  private IndexMap newIndexMap(Range2 extent) {
    checkExtent(extent);
    if (extent.getWidth() == 1) {
      return new VerticalIndexMap(extent);
    }
    if (extent.getHeight() == 1) {
      return new HorizontalIndexMap(extent);
    }
    throw new IllegalArgumentException("ViewStrip extent must be only one button wide or high");
  }

  private void checkExtent(Range2 extent) {
    if (!baseViewport.getExtent().isRangeWithin(extent)) {
      throw new IllegalArgumentException(
          "Extent extends beyond base viewport: " + extent.toString());
    }
  }

  @Override
  public Range1 getExtent() {
    return indexMap.getExtent();
  }

  @Override
  public void setLight(int index, Color color) {
    baseViewport.setLight(indexMap.getX(index), indexMap.getY(index), color);
  }

  @Override
  public void addListener(ViewStripListener listener) {
    baseViewport.addListener(
        new ViewportListener() {
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

    Range1 getExtent();
  }

  private static class HorizontalIndexMap implements IndexMap {
    private int xLow;
    private int xHigh;
    private int yVal;
    private Range1 extent;

    public HorizontalIndexMap(Range2 extent) {
      xLow = extent.xRange().low();
      xHigh = extent.xRange().high();
      yVal = extent.yRange().low();
      this.extent = Range1.create(xLow, xHigh);
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
    public Range1 getExtent() {
      return extent;
    }
  }

  private static class VerticalIndexMap implements IndexMap {
    private int xVal;
    private int yLow;
    private int yHigh;
    private Range1 extent;

    public VerticalIndexMap(Range2 extent) {
      xVal = extent.xRange().low();
      yLow = extent.yRange().low();
      yHigh = extent.yRange().high();
      this.extent = Range1.create(yLow, yHigh);
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
    public Range1 getExtent() {
      return extent;
    }
  }
}
