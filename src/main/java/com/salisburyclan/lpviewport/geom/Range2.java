package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;

/** Encodes a 2-D xrange and yrange. */
@AutoValue
public abstract class Range2 {
  // Builds an extent from coordinates.
  public static Range2 create(Point low, Point high) {
    return create(Range.create(low.x(), high.x()), Range.create(low.y(), high.y()));
  }

  // Builds an extent from coordinates.
  public static Range2 create(int xLow, int yLow, int xHigh, int yHigh) {
    return create(Range.create(xLow, xHigh), Range.create(yLow, yHigh));
  }

  // Builds an extent from ranges.
  public static Range2 create(Range xRange, Range yRange) {
    return new AutoValue_Range2(xRange, yRange);
  }

  public abstract Range xRange();

  public abstract Range yRange();

  public int getWidth() {
    return xRange().size();
  }

  public int getHeight() {
    return yRange().size();
  }

  // Throws IllegalArgumentException if given point is not within this extent.
  public void assertPointWithin(Point p) {
    if (!isPointWithin(p)) {
      throw new IllegalArgumentException(String.format("%s out of extent range %s", p, this));
    }
  }

  // Returns true iff given point is within this extent.
  public boolean isPointWithin(Point p) {
    return xRange().isPointWithin(p.x()) && yRange().isPointWithin(p.y());
  }

  // Returns true iff other extent is within this extent.
  public boolean isRangeWithin(Range2 other) {
    return xRange().isRangeWithin(other.xRange()) && yRange().isRangeWithin(other.yRange());
  }

  public Range2 inset(int xLowInset, int yLowInset, int xHighInset, int yHighInset) {
    return create(xRange().inset(xLowInset, xHighInset), yRange().inset(yLowInset, yHighInset));
  }

  public Range2 shift(int xOffset, int yOffset) {
    return create(xRange().shift(xOffset), yRange().shift(yOffset));
  }

  // Returns a new ViewExtent that includes the full range of both this and other.
  public Range2 includeBoth(Range2 other) {
    return create(xRange().includeBoth(other.xRange()), yRange().includeBoth(other.yRange()));
  }

  /*
  // TODO: clean up
  @Override
  public String toString() {
    return String.format("ViewExtent(%s,%s)-(%s,%s)", xLow, yLow, xHigh, yHigh);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ViewExtent)) {
      return false;
    }
    ViewExtent o = (ViewExtent) other;
    return xLow == o.getXLow()
        && yLow == o.getYLow()
        && xHigh == o.getXHigh()
        && yHigh == o.getYHigh();
  }

  @Override
  public int hashCode() {
    return 41 * (41 * (41 * getXLow() + getYLow()) + getXHigh()) + getYHigh();
  }
  */
}
