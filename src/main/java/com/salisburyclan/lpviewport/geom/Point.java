package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point {
  public static Point create(int x, int y) {
    return new AutoValue_Point(x, y);
  }

  public abstract int x();

  public abstract int y();
}
