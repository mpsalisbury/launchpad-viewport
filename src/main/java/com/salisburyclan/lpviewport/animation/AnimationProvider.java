package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.AnimatedLayer;

public interface AnimationProvider {
  AnimatedLayer newAnimation(Range2 extent);
}
