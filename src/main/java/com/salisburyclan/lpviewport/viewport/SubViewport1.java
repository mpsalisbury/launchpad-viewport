package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;
import java.util.HashMap;
import java.util.Map;

// A viewport that represents a 1-dimensional sub-rectangle of an existing viewport.
// Index is in the long-dimension range.
public class SubViewport1 implements Viewport1 {
  private Viewport baseViewport;
  private WriteLayer writeLayer;
  private Range1 extent;
  private IndexMap indexMap;
  // Keep track of derived listeners so we can remove them
  // from baseViewport upon request.
  private Map<Button1Listener, Button2Listener> listenerMap;

  public SubViewport1(Viewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.writeLayer = baseViewport.addLayer();
    this.indexMap = newIndexMap(extent);
    this.listenerMap = new HashMap<>();
  }

  private IndexMap newIndexMap(Range2 extent) {
    checkExtent(extent);
    if (extent.getWidth() == 1) {
      return new VerticalIndexMap(extent);
    }
    if (extent.getHeight() == 1) {
      return new HorizontalIndexMap(extent);
    }
    throw new IllegalArgumentException("Viewport1 extent must be only one button wide or high");
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
  public void setPixel(int index, Pixel pixel) {
    writeLayer.setPixel(indexMap.getPoint(index), pixel);
  }

  @Override
  public void addListener(Button1Listener listener) {
    Button2Listener subListener =
        new Button2Listener() {
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
        };
    listenerMap.put(listener, subListener);
    baseViewport.addListener(subListener);
  }

  @Override
  public void removeListener(Button1Listener listener) {
    Button2Listener subListener = listenerMap.remove(listener);
    if (subListener != null) {
      baseViewport.removeListener(subListener);
    }
  }

  @Override
  public void removeAllListeners() {
    listenerMap.values().forEach(subListener -> baseViewport.removeListener(subListener));
    listenerMap.clear();
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
      if (!xExtent.isPointWithin(index)) {
        throw new IllegalArgumentException("SubViewport1 index out of range: " + index);
      }
      return Point.create(index, yVal);
    }

    @Override
    public boolean isPointWithin(Point p) {
      return xExtent.isPointWithin(p.x()) && (p.y() == yVal);
    }

    @Override
    public int getIndex(Point p) {
      return p.x();
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
      if (!yExtent.isPointWithin(index)) {
        throw new IllegalArgumentException("SubViewport1 index out of range: " + index);
      }
      return Point.create(xVal, index);
    }

    @Override
    public boolean isPointWithin(Point p) {
      return yExtent.isPointWithin(p.y()) && (p.x() == xVal);
    }

    @Override
    public int getIndex(Point p) {
      return p.y();
    }

    @Override
    public Range1 getExtent() {
      return yExtent;
    }
  }
}
