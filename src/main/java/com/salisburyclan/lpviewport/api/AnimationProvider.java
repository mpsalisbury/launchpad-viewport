package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;

// Provides new animations of the given extent.
public interface AnimationProvider {
  // Returns a newly constructed animation of the given extent.
  AnimatedLayer newAnimation(Range2 extent);
}
