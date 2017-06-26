package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.util.Multiplexer;

/** Forwards PixelListener calls to a set of PixelListeners. */
public class PixelListenerMultiplexer extends Multiplexer<PixelListener> implements PixelListener {

  public void onNextFrame() {
    getItemsCopy().forEach(listener -> listener.onNextFrame());
  }

  public void onSetPixel(int x, int y) {
    getItemsCopy().forEach(listener -> listener.onSetPixel(x, y));
  }
}
