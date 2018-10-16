package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// An animated layer that supports frames (draw/erase cycles).
public abstract class FramedAnimation extends AnimatedLayer {
  private LayerBuffer layer;

  // @param extent The extent represented by this layer.
  public FramedAnimation(Range2 extent) {
    layer = new LayerBuffer(extent);
  }

  @Override
  protected ReadLayer getReadLayer() {
    return layer;
  }

  protected WriteLayer getWriteLayer() {
    return layer;
  }

  /*
    @Override
    public Pixel getPixel(int x, int y) {
      return layer.getPixel(x, y);
    }

    @Override
    public Range2 getExtent() {
      return layer.getExtent();
    }

    @Override
    public void addPixelListener(PixelListener listener) {
      layer.addPixelListener(listener);
    }

    @Override
    public void removePixelListener(PixelListener listener) {
      layer.removePixelListener(listener);
    }

    @Override
    public void removeAllPixelListeners() {
      layer.removeAllPixelListeners();
    }

    @Override
    public void addCloseListener(CloseListener listener) {
      layer.addCloseListener(listener);
    }
  */
}
