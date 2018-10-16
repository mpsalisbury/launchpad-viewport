package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@AutoValue
public abstract class Range1 {
  public static final Range1 EMPTY = new AutoValue_Range1(1, 0);

  public static Range1 create(int low, int high) {
    if (low <= high) {
      return new AutoValue_Range1(low, high);
    } else {
      return EMPTY;
    }
  }

  public abstract int low();

  public abstract int high();

  // No values in this range.
  public boolean isEmpty() {
    return low() > high();
  }

  public int middle() {
    if (isEmpty()) {
      return 0;
    }
    return (high() + low()) / 2;
  }

  public int size() {
    if (isEmpty()) {
      return 0;
    }
    return high() - low() + 1;
  }

  public boolean isPointWithin(int p) {
    return (p >= low()) && (p <= high());
  }

  // Throws IllegalArgumentException if given point is not within this extent.
  public void assertPointWithin(int p) {
    if (!isPointWithin(p)) {
      throw new IllegalArgumentException(
          String.format("point(%s) out of extent range (%s, %s)", p, low(), high()));
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

  public Range1 union(Range1 other) {
    if (other.isEmpty()) {
      return this;
    }
    if (this.isEmpty()) {
      return other;
    }
    return create(Math.min(low(), other.low()), Math.max(high(), other.high()));
  }

  public Range1 intersect(Range1 other) {
    return create(Math.max(low(), other.low()), Math.min(high(), other.high()));
  }

  public IntStream stream() {
    return IntStream.rangeClosed(low(), high());
  }

  // Iterate through all values.
  public void forEach(Consumer<Integer> callback) {
    for (int x = low(); x <= high(); ++x) {
      callback.accept(x);
    }
  }
}
