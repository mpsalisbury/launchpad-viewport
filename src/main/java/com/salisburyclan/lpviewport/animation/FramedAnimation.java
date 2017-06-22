package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.CloseListener;
import com.salisburyclan.lpviewport.layer.FrameWriteLayer;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.LayerBuffer;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.PixelListener;

public abstract class FramedAnimation extends Animation implements Layer {
  private LayerBuffer layer;

  public FramedAnimation(Range2 extent) {
    layer = new LayerBuffer(extent);
  }

  protected FrameWriteLayer getWriteLayer() {
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
