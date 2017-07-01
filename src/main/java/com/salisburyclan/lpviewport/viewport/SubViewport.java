package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements Viewport {
  private Viewport baseViewport;
  private Range2 subExtent;
  private Vector originOffset;

  public SubViewport(Viewport baseViewport, Range2 subExtent) {
    this.baseViewport = baseViewport;
    this.subExtent = subExtent;
    this.originOffset = subExtent.origin().subtract(Point.create(0, 0));
    checkExtent(subExtent);
  }

  private void checkExtent(Range2 subExtent) {
    if (!baseViewport.getExtent().isRangeWithin(subExtent)) {
      throw new IllegalArgumentException(
          "Extent extends beyond base viewport: " + subExtent.toString());
    }
  }

  @Override
  public Range2 getExtent() {
    return subExtent;
  }

  @Override
  public LayerBuffer addLayer() {
    LayerBuffer subLayer = new LayerBuffer(subExtent);
    addLayer(subLayer);
    return subLayer;
  }

  @Override
  public void addLayer(ReadLayer layer) {
    baseViewport.addLayer(layer);
    layer.addCloseListener(() -> removeLayer(layer));
  }

  @Override
  public void removeLayer(ReadLayer layer) {
    baseViewport.removeLayer(layer);
  }

  @Override
  public void addListener(Button2Listener listener) {
    baseViewport.addListener(
        new Button2Listener() {
          public void onButtonPressed(Point p) {
            if (subExtent.isPointWithin(p)) {
              listener.onButtonPressed(p);
            }
          }

          public void onButtonReleased(Point p) {
            if (subExtent.isPointWithin(p)) {
              listener.onButtonReleased(p);
            }
          }
        });
  }

  @Override
  public void removeListener(Button2Listener listener) {
    // TODO implement
    throw new UnsupportedOperationException("SubViewport::removeListener");
  }
}
