package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.util.Multiplexer;

/** Forwards CloseListener calls to a set of CloseListeners. */
public class CloseListenerMultiplexer extends Multiplexer<CloseListener> implements CloseListener {

  public void onClose() {
    getItemsCopy().forEach(listener -> listener.onClose());
  }
}
