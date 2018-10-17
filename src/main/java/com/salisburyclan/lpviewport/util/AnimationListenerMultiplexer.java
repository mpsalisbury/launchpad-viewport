package com.salisburyclan.lpviewport.util;

import com.salisburyclan.lpviewport.api.AnimationListener;

/** Forwards AnimationListener calls to a set of AnimationListeners. */
public class AnimationListenerMultiplexer extends Multiplexer<AnimationListener>
    implements AnimationListener {

  public void onFinished() {
    getItemsCopy().forEach(listener -> listener.onFinished());
  }
}
