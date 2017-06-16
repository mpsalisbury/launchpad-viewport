package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.geom.Point;
import java.util.ArrayList;
import java.util.List;

/** Forwards Button2Listener calls to a set of Button2Listeners. */
public class ListenerMultiplexer implements Button2Listener {

  private List<Button2Listener> listeners = new ArrayList<>();

  public void addListener(Button2Listener listener) {
    listeners.add(listener);
  }

  public void removeListener(Button2Listener listener) {
    listeners.remove(listener);
  }

  public void onButtonPressed(Point p) {
    List<Button2Listener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onButtonPressed(p));
  }

  public void onButtonReleased(Point p) {
    List<Button2Listener> listenersDefensiveCopy = new ArrayList<>(listeners);
    listenersDefensiveCopy.forEach(listener -> listener.onButtonReleased(p));
  }
}
