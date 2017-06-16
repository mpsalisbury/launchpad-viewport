package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.RawViewport;

public interface AnimationProvider {
  Animation newAnimation(RawViewport viewport);
}
