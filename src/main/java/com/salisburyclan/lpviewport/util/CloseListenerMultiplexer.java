package com.salisburyclan.lpviewport.util;

import com.salisburyclan.lpviewport.api.CloseListener;

/** Forwards CloseListener calls to a set of CloseListeners. */
public class CloseListenerMultiplexer extends Multiplexer<CloseListener> implements CloseListener {

  public void onClose() {
    getItemsCopy().forEach(listener -> listener.onClose());
  }
}
