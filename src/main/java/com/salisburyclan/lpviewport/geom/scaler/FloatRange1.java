package com.salisburyclan.lpviewport.geom.scaler;

import com.google.auto.value.AutoValue;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.scaler.Rounder.RoundingPolicy;

// A 1D range of floating-point positions.
@AutoValue
public abstract class FloatRange1 {
  public static final FloatRange1 EMPTY = new AutoValue_FloatRange1(0.0f, 0.0f);

  public static FloatRange1 create(Range1 range) {
    return create(range.low() - 0.5f, range.high() + 0.5f);
  }

  public static FloatRange1 create(float low, float high) {
    if (low <= high) {
      return new AutoValue_FloatRange1(low, high);
    } else {
      return EMPTY;
    }
  }

  public abstract float low();

  public abstract float high();

  // No values in this range.
  public boolean isEmpty() {
    return low() >= high();
  }

  public float size() {
    return high() - low();
  }

  @FunctionalInterface
  public interface WeightCallback {
    void apply(int x, float weight);
  }

  // For each int range in this float range, callback with the int value and a weighted coverage
  // representing how much of the int range is covered by the float range.
  public void weightedIterator(WeightCallback callback) {
    int lowInt = Rounder.round(low(), RoundingPolicy.ROUND_DOWN);
    int highInt = Rounder.round(high(), RoundingPolicy.ROUND_DOWN);
    if (lowInt == highInt) {
      // Whole range covered by single pixel.
      float weight = high() - low();
      if (weight > 0.0) {
        callback.apply(lowInt, weight);
      }
      return;
    }
    callback.apply(lowInt, (float) lowInt + 1.0f - low());

    for (int x = lowInt + 1; x < highInt; ++x) {
      callback.apply(x, 1.0f);
    }

    {
      // Remaining partial pixel.
      float weight = high() - highInt;
      if (weight > 0.0) {
        callback.apply(highInt, weight);
      }
    }
  }
}
