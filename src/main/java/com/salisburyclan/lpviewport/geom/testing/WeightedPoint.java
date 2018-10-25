package com.salisburyclan.lpviewport.geom.testing;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;

// A point's coordinates and its weight.
@AutoValue
public abstract class WeightedPoint {
  public static WeightedPoint create(int x, int y, double weight) {
    return new AutoValue_WeightedPoint(x, y, weight);
  }

  public abstract int x();

  public abstract int y();

  public abstract double weight();

  public boolean equals(Object o) {
    if (o instanceof WeightedPoint) {
      WeightedPoint other = (WeightedPoint) o;
      return x() == other.x()
          && y() == other.y()
          && DoubleMath.fuzzyEquals(weight(), other.weight(), 1e-5);
    }
    return false;
  }
}
