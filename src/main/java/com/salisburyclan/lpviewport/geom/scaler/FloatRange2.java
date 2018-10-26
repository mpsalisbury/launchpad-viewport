package com.salisburyclan.lpviewport.geom.scaler;

import com.google.auto.value.AutoValue;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Range2Scaler;

// A 2D range of floating-point positions.
@AutoValue
public abstract class FloatRange2 {
  public static final FloatRange2 EMPTY =
      new AutoValue_FloatRange2(FloatRange1.EMPTY, FloatRange1.EMPTY);

  public static FloatRange2 create(Range2 range) {
    return create(FloatRange1.create(range.xRange()), FloatRange1.create(range.yRange()));
  }

  // Builds an extent from coordinates.
  public static FloatRange2 create(FloatPoint low, FloatPoint high) {
    return create(FloatRange1.create(low.x(), high.x()), FloatRange1.create(low.y(), high.y()));
  }

  // Builds an extent from coordinates.
  public static FloatRange2 create(float xLow, float yLow, float xHigh, float yHigh) {
    return create(FloatRange1.create(xLow, xHigh), FloatRange1.create(yLow, yHigh));
  }

  // Builds an extent from ranges.
  public static FloatRange2 create(FloatRange1 xRange, FloatRange1 yRange) {
    if (xRange.isEmpty() || yRange.isEmpty()) {
      return EMPTY;
    }
    return new AutoValue_FloatRange2(xRange, yRange);
  }

  public abstract FloatRange1 xRange();

  public abstract FloatRange1 yRange();

  public FloatPoint low() {
    return FloatPoint.create(xRange().low(), yRange().low());
  }

  public FloatPoint high() {
    return FloatPoint.create(xRange().high(), yRange().high());
  }

  public boolean isEmpty() {
    return xRange().isEmpty() || yRange().isEmpty();
  }

  public float getWidth() {
    return xRange().size();
  }

  public float getHeight() {
    return yRange().size();
  }

  public Range2 round() {
    return Range2.create(xRange().round(), yRange().round());
  }

  // For each int range in this float range, callback with the Point and a weighted coverage
  // representing how much of the Point range is covered by the float range.
  public void weightedIterator(Range2Scaler.WeightedPointCallback callback) {
    // For each weighted X position ...
    xRange()
        .weightedIterator(
            (x, xWeight) -> {
              // for each weighted Y position ...
              yRange()
                  .weightedIterator(
                      (y, yWeight) -> {
                        callback.apply(x, y, xWeight * yWeight);
                      });
            });
  }
}
