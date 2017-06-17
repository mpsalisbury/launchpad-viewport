package com.salisburyclan.lpviewport.layer;

import java.util.ArrayList;
import java.util.List;

/** Forwards CloseListener calls to a set of CloseListeners. */
public class CloseListenerMultiplexer implements CloseListener {

  private List<CloseListener> listeners = new ArrayList<>();

  public void addListener(CloseListener listener) {
    listeners.add(listener);
  }

  public void removeListener(CloseListener listener) {
    listeners.remove(listener);
  }

  public void onClose() {
    List<CloseListener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onClose());
  }
}
