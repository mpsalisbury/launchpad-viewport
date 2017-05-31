package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;
import java.util.stream.IntStream;

@AutoValue
public abstract class Range1 {
  public static Range1 create(int low, int high) {
    assertValid(low, high);
    return new AutoValue_Range1(low, high);
  }

  // Validates that the given extentLow and extentHigh are valid values.
  private static void assertValid(int low, int high) {
    if (low > high) {
      throw new IllegalArgumentException("Invalid extents: " + low + "-" + high);
    }
  }

  public abstract int low();

  public abstract int high();

  public int size() {
    return high() - low() + 1;
  }

  public boolean isPointWithin(int p) {
    return (p >= low()) && (p <= high());
  }

  // Throws IllegalArgumentException if given point is not within this extent.
  public void assertPointWithin(int p) {
    if (!isPointWithin(p)) {
      throw new IllegalArgumentException(
          String.format("point(%s) out of extent range (%s, %s)", x, low, high));
    }
  }

  public boolean isRangeWithin(Range1 other) {
    return isPointWithin(other.low()) && isPointWithin(other.high());
  }

  public Range1 inset(int lowInset, int highInset) {
    return create(low() + lowInset, high() - highInset);
  }

  public Range1 shift(int offset) {
    return create(low() + offset, high() + offset);
  }

  public Range1 includeBoth(Range1 other) {
    return create(Math.min(low(), other.low()), Math.max(high(), other.high()));
  }

  public IntStream stream() {
    return IntStream.rangeClosed(low(), high());
  }
}
