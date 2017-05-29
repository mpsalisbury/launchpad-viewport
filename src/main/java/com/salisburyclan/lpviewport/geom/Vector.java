package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Vector {
  public static Vector create(int dx, int dy) {
    return new AutoValue_Vector(dx, dy);
  }

  public abstract int dx();

  public abstract int dy();

  public Point add(Point p) {
    return Point.create(p.x() + dx(), p.y() + dy());
  }

  public Vector add(Vector v) {
    return Vector.create(v.dx() + dx(), v.dy() + dy());
  }
}
