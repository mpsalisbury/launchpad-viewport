package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Range {
  public static Range create(int low, int high) {
    return new AutoValue_Range(low, high);
  }

  public abstract int low();

  public abstract int high();
}
