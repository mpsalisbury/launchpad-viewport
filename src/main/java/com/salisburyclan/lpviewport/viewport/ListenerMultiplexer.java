package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.ViewportListener;
import java.util.ArrayList;
import java.util.List;

/** Forwards ViewportListener calls to a set of ViewportListeners. */
public class ListenerMultiplexer implements ViewportListener {

  private List<ViewportListener> listeners = new ArrayList<>();

  public void addListener(ViewportListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ViewportListener listener) {
    listeners.remove(listener);
  }

  public void onButtonPressed(int xpos, int ypos) {
    List<ViewportListener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onButtonPressed(xpos, ypos));
  }

  public void onButtonReleased(int xpos, int ypos) {
    List<ViewportListener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onButtonReleased(xpos, ypos));
  }
}
