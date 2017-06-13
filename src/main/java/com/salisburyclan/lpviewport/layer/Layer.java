package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Range2;

// An image layer holding a set of readable pixels and transparencies.
public interface Layer {
  // Returns the pixel extent for this layer.
  public Range2 getExtent();

  // Reads one pixel in this layer.
  public Color getColor(int x, int y);

  public double getAlpha(int x, int y);
}
