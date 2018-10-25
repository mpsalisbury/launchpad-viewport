package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

// Describes a 2D point.
@AutoValue
public abstract class Point {
  public static Point create(int x, int y) {
    return new AutoValue_Point(x, y);
  }

  public abstract int x();

  public abstract int y();

  public Point add(Vector v) {
    return Point.create(x() + v.dx(), y() + v.dy());
  }

  public Point subtract(Vector v) {
    return Point.create(x() - v.dx(), y() - v.dy());
  }

  public Vector subtract(Point p) {
    return Vector.create(x() - p.x(), y() - p.y());
  }
}
