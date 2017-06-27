package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

public interface AnimationProvider {
  AnimatedLayer newAnimation(Range2 extent);
}
