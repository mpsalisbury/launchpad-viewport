package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;
import java.util.stream.IntStream;

@AutoValue
public abstract class Range {
  public static Range create(int low, int high) {
    assertValid(low, high);
    return new AutoValue_Range(low, high);
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

  public boolean isRangeWithin(Range other) {
    return isPointWithin(other.low()) && isPointWithin(other.high());
  }

  public Range inset(int lowInset, int highInset) {
    return create(low() + lowInset, high() - highInset);
  }

  public Range shift(int offset) {
    return create(low() + offset, high() + offset);
  }

  public Range includeBoth(Range other) {
    return create(Math.min(low(), other.low()), Math.max(high(), other.high()));
  }

  public IntStream stream() {
    return IntStream.rangeClosed(low(), high());
  }
}
