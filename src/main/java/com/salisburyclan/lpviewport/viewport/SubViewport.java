package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import java.util.HashMap;
import java.util.Map;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements Viewport {
  private Viewport baseViewport;
  private Range2 subExtent;
  // Keep track of derived listeners so we can remove them
  // from baseViewport upon request.
  private Map<Button2Listener, Button2Listener> listenerMap;

  public SubViewport(Viewport baseViewport, Range2 subExtent) {
    this.baseViewport = baseViewport;
    this.subExtent = subExtent;
    checkExtent(subExtent);
    this.listenerMap = new HashMap<>();
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
    Button2Listener subListener =
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
        };
    listenerMap.put(listener, subListener);
    baseViewport.addListener(subListener);
  }

  @Override
  public void removeListener(Button2Listener listener) {
    Button2Listener subListener = listenerMap.remove(listener);
    if (subListener != null) {
      baseViewport.removeListener(subListener);
    }
  }
}
