package com.salisburyclan.lpviewport.layer;

import java.util.ArrayList;
import java.util.List;

/** Forwards PixelListener calls to a set of PixelListeners. */
public class PixelListenerMultiplexer implements PixelListener {

  private List<PixelListener> listeners = new ArrayList<>();

  public void addListener(PixelListener listener) {
    listeners.add(listener);
  }

  public void removeListener(PixelListener listener) {
    listeners.remove(listener);
  }

  public void onSetPixel(int x, int y) {
    List<PixelListener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onSetPixel(x, y));
  }
}
