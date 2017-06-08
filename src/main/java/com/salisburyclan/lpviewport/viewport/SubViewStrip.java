package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.api.ViewStripListener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
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
    baseViewport.setLight(indexMap.getPoint(index), color);
  }

  @Override
  public void addListener(ViewStripListener listener) {
    baseViewport.addListener(
        new ViewportListener() {
          public void onButtonPressed(Point p) {
            if (indexMap.isPointWithin(p)) {
              listener.onButtonPressed(indexMap.getIndex(p));
            }
          }

          public void onButtonReleased(Point p) {
            if (indexMap.isPointWithin(p)) {
              listener.onButtonReleased(indexMap.getIndex(p));
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
    Point getPoint(int index);

    boolean isPointWithin(Point p);

    int getIndex(Point p);

    Range1 getExtent();
  }

  private static class HorizontalIndexMap implements IndexMap {
    private Range1 xExtent;
    private int yVal;

    public HorizontalIndexMap(Range2 extent) {
      xExtent = extent.xRange();
      yVal = extent.yRange().low();
    }

    @Override
    public Point getPoint(int index) {
      return Point.create(xExtent.low() + index, yVal);
    }

    @Override
    public boolean isPointWithin(Point p) {
      return xExtent.isPointWithin(p.x()) && (p.y() == yVal);
    }

    @Override
    public int getIndex(Point p) {
      return p.x() - xExtent.low();
    }

    @Override
    public Range1 getExtent() {
      return xExtent;
    }
  }

  private static class VerticalIndexMap implements IndexMap {
    private int xVal;
    private Range1 yExtent;

    public VerticalIndexMap(Range2 extent) {
      xVal = extent.xRange().low();
      yExtent = extent.yRange();
    }

    @Override
    public Point getPoint(int index) {
      return Point.create(xVal, yExtent.low() + index);
    }

    @Override
    public boolean isPointWithin(Point p) {
      return yExtent.isPointWithin(p.y()) && (p.x() == xVal);
    }

    @Override
    public int getIndex(Point p) {
      return p.y() - yExtent.low();
    }

    @Override
    public Range1 getExtent() {
      return yExtent;
    }
  }
}
