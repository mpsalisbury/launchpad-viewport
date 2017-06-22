package com.salisburyclan.lpviewport.layer;

// Listen for pixel changes.
public interface PixelListener {
  // Called when layer is ready to start a new frame.
  void onNextFrame();

  // Called when layer sets a pixel.
  void onSetPixel(int x, int y);
}
