package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Range2;

public interface AnimationProvider {
  AnimatedLayer newAnimation(Range2 extent);
}
