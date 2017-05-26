package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Vector {
  public static Vector create(int dx, int dy) {
    return new AutoValue_Vector(dx, dy);
  }

  public abstract int dx();

  public abstract int dy();
}
