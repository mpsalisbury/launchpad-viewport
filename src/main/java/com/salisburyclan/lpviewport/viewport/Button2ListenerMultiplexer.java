package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.util.Multiplexer;

/** Forwards Button2Listener calls to a set of Button2Listeners. */
public class Button2ListenerMultiplexer extends Multiplexer<Button2Listener>
    implements Button2Listener {

  public void onButtonPressed(Point p) {
    getItemsCopy().forEach(listener -> listener.onButtonPressed(p));
  }

  public void onButtonReleased(Point p) {
    getItemsCopy().forEach(listener -> listener.onButtonReleased(p));
  }
}
