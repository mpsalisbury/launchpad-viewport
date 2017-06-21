package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.CloseListener;
import com.salisburyclan.lpviewport.layer.DecayingBuffer;
import com.salisburyclan.lpviewport.layer.DecayingLayer;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.PixelListener;

public abstract class DecayingAnimation extends Animation implements Layer {
  private DecayingLayer layer;
  private DecayingBuffer buffer;

  public DecayingAnimation(Range2 extent) {
    layer = new DecayingLayer(extent);
    buffer = layer.getInputBuffer();
  }

  protected DecayingBuffer getBuffer() {
    return buffer;
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
  public void addCloseListener(CloseListener listener) {
    layer.addCloseListener(listener);
  }
}
