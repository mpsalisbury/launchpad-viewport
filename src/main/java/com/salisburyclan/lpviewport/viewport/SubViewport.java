package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.layer.CloseListener;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.LayerBuffer;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.PixelListener;

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
  public LayerBuffer addLayer() {
    LayerBuffer subLayer = new LayerBuffer(extent);
    addLayer(subLayer);
    return subLayer;
  }

  @Override
  public void addLayer(Layer subLayer) {
    baseViewport.addLayer(new WrappingLayer(subLayer));
  }

  // Wraps subview layer to act like baseview layer.
  private class WrappingLayer implements Layer {
    private Layer innerLayer;

    public WrappingLayer(Layer innerLayer) {
      this.innerLayer = innerLayer;
    }

    @Override
    public Range2 getExtent() {
      return baseViewport.getExtent();
    }

    @Override
    public Pixel getPixel(int x, int y) {
      return innerLayer.getPixel(Point.create(x, y).subtract(originOffset));
    }

    @Override
    public void addPixelListener(PixelListener listener) {
      innerLayer.addPixelListener(
          new PixelListener() {
            @Override
            public void onNextFrame() {
              listener.onNextFrame();
            }

            @Override
            public void onSetPixel(int x, int y) {
              Point p = Point.create(x, y);
              if (extent.isPointWithin(p)) {
                // TODO: check direction of this transform.
                Point offsetP = p.subtract(originOffset);
                listener.onSetPixel(offsetP.x(), offsetP.y());
              }
            }
          });
    }

    @Override
    public void removePixelListener(PixelListener listener) {
      // TODO implement
      throw new UnsupportedOperationException("WrappingLayer::removePixelListener");
    }

    @Override
    public void addCloseListener(CloseListener listener) {
      innerLayer.addCloseListener(listener);
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
