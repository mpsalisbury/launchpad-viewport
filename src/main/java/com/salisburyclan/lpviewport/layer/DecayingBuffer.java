package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Range2;

// A ViewportBuffer for a DecayingLayer;
public class DecayingBuffer extends LayerBuffer {
  private DecayingLayer baseLayer;

  public DecayingBuffer(DecayingLayer baseLayer, Range2 extent) {
    super(extent);
    this.baseLayer = baseLayer;
  }

  public void pushFrame() {
    baseLayer.pushFrame(this);
  }
}
