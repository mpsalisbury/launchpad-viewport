package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Range2;

// An image layer holding a set of readable pixels and transparencies.
public interface Layer {
  // Returns the pixel extent for this layer.
  public Range2 getExtent();

  // Reads one pixel in this layer.
  public Pixel getPixel(int x, int y);

  public void addPixelListener(PixelListener listener);

  public void addCloseListener(CloseListener listener);
}
