package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// A layer that supports animation.
public abstract class ReadLayerProxy implements ReadLayer {

  // Returns the ReadLayer to be proxied by this layer.
  protected abstract ReadLayer getReadLayer();

  @Override
  public Pixel getPixel(int x, int y) {
    return getReadLayer().getPixel(x, y);
  }

  @Override
  public Range2 getExtent() {
    return getReadLayer().getExtent();
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    getReadLayer().addPixelListener(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    getReadLayer().removePixelListener(listener);
  }

  @Override
  public void removeAllPixelListeners() {
    getReadLayer().removeAllPixelListeners();
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    getReadLayer().addCloseListener(listener);
  }
}
