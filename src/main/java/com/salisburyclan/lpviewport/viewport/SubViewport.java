package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements RawViewport {
  private RawViewport baseViewport;
  private LightLayer outputLayer;
  private Range2 extent;
  private Vector originOffset;

  public SubViewport(RawViewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.outputLayer = new SubLightLayer(baseViewport.getLightLayer());
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
  public LightLayer getLightLayer() {
    return outputLayer;
  }

  private class SubLightLayer implements LightLayer {
    private LightLayer baseLightLayer;

    public SubLightLayer(LightLayer baseLightLayer) {
      this.baseLightLayer = baseLightLayer;
    }

    @Override
    public Range2 getExtent() {
      return extent;
    }

    @Override
    public void setLight(int x, int y, Color color) {
      baseLightLayer.setLight(originOffset.add(Point.create(x, y)), color);
    }

    @Override
    public void setAllLights(Color color) {
      extent.forEach((x, y) -> baseLightLayer.setLight(x, y, color));
    }
  }

  @Override
  public void addListener(Button2Listener listener) {
    baseViewport.addListener(
        new Button2Listener() {
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
  public void removeListener(Button2Listener listener) {
    // TODO implement
    throw new UnsupportedOperationException("SubViewport::removeListener");
  }
}
