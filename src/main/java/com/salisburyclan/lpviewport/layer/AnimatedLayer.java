package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.geom.Range2;

public abstract class AnimatedLayer extends Animation implements Layer {
  private LayerBuffer buffer;

  public AnimatedLayer(Range2 extent) {
    this.buffer = new LayerBuffer(extent);
  }

  protected WriteLayer getWriteLayer() {
    return buffer;
  }

  public Range2 getExtent() {
    return buffer.getExtent();
  }

  public Pixel getPixel(int x, int y) {
    return buffer.getPixel(x, y);
  }

  public void addPixelListener(PixelListener listener) {
    buffer.addPixelListener(listener);
  }

  public void removePixelListener(PixelListener listener) {
    buffer.removePixelListener(listener);
  }

  public void addCloseListener(CloseListener listener) {
    buffer.addCloseListener(listener);
  }
}
