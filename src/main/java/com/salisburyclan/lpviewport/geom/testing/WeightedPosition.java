package com.salisburyclan.lpviewport.geom.testing;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;

// A position and its weight.
@AutoValue
public abstract class WeightedPosition {
  public static WeightedPosition create(int x, double weight) {
    return new AutoValue_WeightedPosition(x, weight);
  }

  public abstract int x();

  public abstract double weight();

  public boolean equals(Object o) {
    if (o instanceof WeightedPosition) {
      WeightedPosition other = (WeightedPosition) o;
      return x() == other.x() && DoubleMath.fuzzyEquals(weight(), other.weight(), 1e-5);
    }
    return false;
  }
}
