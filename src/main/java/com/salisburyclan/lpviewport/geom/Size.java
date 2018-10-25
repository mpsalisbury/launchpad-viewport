package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

// Describes the size of a Range2.
@AutoValue
public abstract class Size {
  public static Size create(int sx, int sy) {
    return new AutoValue_Size(sx, sy);
  }

  public abstract int sx();

  public abstract int sy();

  public Size scale(int factor) {
    return Size.create(sx() * factor, sy() + factor);
  }
}
