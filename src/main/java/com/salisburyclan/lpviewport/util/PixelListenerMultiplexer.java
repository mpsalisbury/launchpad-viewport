package com.salisburyclan.lpviewport.util;

import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

/** Forwards PixelListener calls to a set of PixelListeners. */
public class PixelListenerMultiplexer extends Multiplexer<PixelListener> implements PixelListener {

  public void onNextFrame() {
    getItemsCopy().forEach(listener -> listener.onNextFrame());
  }

  public void onPixelChanged(Point p) {
    getItemsCopy().forEach(listener -> listener.onPixelChanged(p));
  }

  public void onPixelsChanged(Range2 range) {
    getItemsCopy().forEach(listener -> listener.onPixelsChanged(range));
  }
}
