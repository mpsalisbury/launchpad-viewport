package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

// Listen for pixel changes.
public interface PixelListener {
  // Called when layer is ready to start a new frame.
  default void onNextFrame() {}

  // Called when layer sets a pixel.
  void onPixelChanged(Point p);

  // Called when layer sets a range of pixels.
  void onPixelsChanged(Range2 range);
}
