package com.salisburyclan.lpviewport.api;

// Reports animation events.
public interface AnimationListener {

  /** Fired when the animation has finished. May never be called if animation goes forever. */
  void onFinished();
}
