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
import com.salisburyclan.lpviewport.layer.PixelListenerMultiplexer;

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

  // TODO consider allowing any sublayer extent of same shape but arbitrary origin.
  @Override
  public void addLayer(Layer subLayer) {
    if (!subLayer.getExtent().equals(subExtent)) {
      throw new IllegalArgumentException("SubViewport::addLayer has incorrect extent");
    }
    Layer wrapLayer = new WrappingLayer(subLayer);
    subLayer.addCloseListener(() -> removeLayer(wrapLayer));
    baseViewport.addLayer(wrapLayer);
  }

  @Override
  public void removeLayer(Layer layer) {
    baseViewport.removeLayer(layer);
  }

  // Wraps subview layer to act like baseview layer.
  private class WrappingLayer implements Layer {
    private Layer subLayer;
    private PixelListenerMultiplexer pixelListeners;

    public WrappingLayer(Layer subLayer) {
      this.subLayer = subLayer;
      this.pixelListeners = new PixelListenerMultiplexer();
      subLayer.addPixelListener(new ShiftingPixelListener());
    }

    @Override
    public Range2 getExtent() {
      return baseViewport.getExtent();
    }

    @Override
    public Pixel getPixel(int x, int y) {
      return subLayer.getPixel(Point.create(x, y));
      //return subLayer.getPixel(Point.create(x, y).subtract(originOffset));
    }

    @Override
    public void addPixelListener(PixelListener listener) {
      pixelListeners.add(listener);
    }

    private class ShiftingPixelListener implements PixelListener {
      @Override
      public void onNextFrame() {
        System.out.println("Got onNextFrame()");
        pixelListeners.onNextFrame();
      }

      @Override
      public void onSetPixel(int x, int y) {
        System.out.println("Got onSetPixel " + x + "," + y);
        Point p = Point.create(x, y);
        if (subExtent.isPointWithin(p)) {
          // TODO: check direction of this transform.
          //          Point offsetP = p.subtract(originOffset);
          //          pixelListeners.onSetPixel(offsetP.x(), offsetP.y());
          pixelListeners.onSetPixel(x, y);
        }
      }
    };

    @Override
    public void removePixelListener(PixelListener listener) {
      // TODO implement
      throw new UnsupportedOperationException("WrappingLayer::removePixelListener");
    }

    @Override
    public void addCloseListener(CloseListener listener) {
      subLayer.addCloseListener(listener);
    }
  }

  @Override
  public void addListener(Button2Listener listener) {
    baseViewport.addListener(
        new Button2Listener() {
          public void onButtonPressed(Point p) {
            if (subExtent.isPointWithin(p)) {
              listener.onButtonPressed(p);
              //listener.onButtonPressed(p.subtract(originOffset));
            }
          }

          public void onButtonReleased(Point p) {
            if (subExtent.isPointWithin(p)) {
              listener.onButtonReleased(p);
              //listener.onButtonReleased(p.subtract(originOffset));
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
