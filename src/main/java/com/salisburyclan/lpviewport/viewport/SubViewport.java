package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements Viewport {
  private Viewport baseViewport;
  private Range2 extent;

  public SubViewport(Viewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.extent = extent;
    checkExtent(extent);
  }

  private void checkExtent(Range2 extent) {
    if (!baseViewport.getExtent().isRangeWithin(extent)) {
      throw new IllegalArgumentException(
          "Extent extends beyond base viewport: " + extent.toString());
    }
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    baseViewport.setLight(extent.origin().add(Vector.create(x, y)), color);
  }

  @Override
  public void setAllLights(Color color) {
    extent
        .xRange()
        .stream()
        .forEach(
            x -> {
              extent
                  .yRange()
                  .stream()
                  .forEach(
                      y -> {
                        baseViewport.setLight(x, y, color);
                      });
            });
  }

  @Override
  public void addListener(ViewportListener listener) {
    baseViewport.addListener(
        new ViewportListener() {
          public void onButtonPressed(int x, int y) {
            if (extent.isPointWithin(Point.create(x, y))) {
              listener.onButtonPressed(x - extent.xRange().low(), y - extent.yRange().low());
            }
          }

          public void onButtonReleased(int x, int y) {
            if (extent.isPointWithin(Point.create(x, y))) {
              listener.onButtonReleased(x - extent.xRange().low(), y - extent.yRange().low());
            }
          }
        });
  }

  @Override
  public void removeListener(ViewportListener listener) {
    // TODO implement
    throw new UnsupportedOperationException("SubViewport::removeListener");
  }
}
