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
  private Vector originOffset;

  public SubViewport(Viewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.extent = extent;
    this.originOffset = extent.origin().subtract(Point.create(0, 0));
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
    baseViewport.setLight(originOffset.add(Point.create(x, y)), color);
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
          public void onButtonPressed(Point p) {
            if (extent.isPointWithin(p)) {
              listener.onButtonPressed(p.subtract(originOffset));
            }
          }

          public void onButtonReleased(Point p) {
            if (extent.isPointWithin(p)) {
              listener.onButtonReleased(p.subtract(originOffset));
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
