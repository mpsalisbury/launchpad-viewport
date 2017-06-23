package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;

// A viewport that represents a sub-rectangle of an existing viewport.
public class SubViewport implements Viewport {
  private Viewport baseViewport;
  private WriteLayer outputLayer;
  private Range2 extent;
  private Vector originOffset;

  public SubViewport(Viewport baseViewport, Range2 extent) {
    this.baseViewport = baseViewport;
    this.outputLayer = new SubWriteLayer(baseViewport.addLayer());
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

 // LayerBuffer addLayer();
 // void addLayer(Layer layer);

  private class SubWriteLayer implements WriteLayer {
    private WriteLayer baseWriteLayer;

    public SubWriteLayer(WriteLayer baseWriteLayer) {
      this.baseWriteLayer = baseWriteLayer;
    }

    @Override
    public Range2 getExtent() {
      return extent;
    }

    @Override
    public void setPixel(int x, int y, Pixel pixel) {
      baseWriteLayer.setPixel(originOffset.add(Point.create(x, y)), pixel);
    }

    @Override
    public void close() {
      // Don't close parent.
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
