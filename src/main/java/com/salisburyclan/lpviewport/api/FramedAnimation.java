package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

public abstract class FramedAnimation extends AnimatedLayer {
  private LayerBuffer layer;

  public FramedAnimation(Range2 extent) {
    layer = new LayerBuffer(extent);
  }

  protected WriteLayer getWriteLayer() {
    return layer;
  }

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
  public void addCloseListener(CloseListener listener) {
    layer.addCloseListener(listener);
  }
}
