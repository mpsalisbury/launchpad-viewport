package com.salisburyclan.lpviewport.geom.scaler;

import com.google.auto.value.AutoValue;
import com.salisburyclan.lpviewport.geom.Point;

// A floating-point position.
@AutoValue
public abstract class FloatPoint {
  public static FloatPoint create(float x, float y) {
    return new AutoValue_FloatPoint(x, y);
  }

  public static FloatPoint create(Point p) {
    return create(p.x(), p.y());
  }

  public abstract float x();

  public abstract float y();
}
