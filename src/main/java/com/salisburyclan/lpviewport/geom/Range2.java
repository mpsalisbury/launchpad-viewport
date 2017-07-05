package com.salisburyclan.lpviewport.geom;

import com.google.auto.value.AutoValue;
import java.util.function.BiConsumer;

/** Encodes a 2-D xrange and yrange. */
@AutoValue
public abstract class Range2 {
  // Builds an extent from coordinates.
  public static Range2 create(Point low, Point high) {
    return create(Range1.create(low.x(), high.x()), Range1.create(low.y(), high.y()));
  }

  // Builds an extent from coordinates.
  public static Range2 create(int xLow, int yLow, int xHigh, int yHigh) {
    return create(Range1.create(xLow, xHigh), Range1.create(yLow, yHigh));
  }

  public static Range2 create(Range1 xRange, int y) {
    return new AutoValue_Range2(xRange, Range1.create(y, y));
  }

  public static Range2 create(int x, Range1 yRange) {
    return new AutoValue_Range2(Range1.create(x, x), yRange);
  }

  // Builds an extent from ranges.
  public static Range2 create(Range1 xRange, Range1 yRange) {
    return new AutoValue_Range2(xRange, yRange);
  }

  public abstract Range1 xRange();

  public abstract Range1 yRange();

  public Point origin() {
    return Point.create(xRange().low(), yRange().low());
  }

  public Point middle() {
    return Point.create(xRange().middle(), yRange().middle());
  }

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
    return isPointWithin(p.x(), p.y());
  }

  public boolean isPointWithin(int x, int y) {
    return xRange().isPointWithin(x) && yRange().isPointWithin(y);
  }

  // Returns true iff other extent is within this extent.
  public boolean isRangeWithin(Range2 other) {
    return xRange().isRangeWithin(other.xRange()) && yRange().isRangeWithin(other.yRange());
  }

  // Return a range inset from this range by the given amounts.
  public Range2 inset(int xLowInset, int yLowInset, int xHighInset, int yHighInset) {
    return create(xRange().inset(xLowInset, xHighInset), yRange().inset(yLowInset, yHighInset));
  }

  public Range2 shift(Vector offset) {
    return create(xRange().shift(offset.dx()), yRange().shift(offset.dy()));
  }

  // Returns a new ViewExtent that includes the full range of both this and other.
  public Range2 includeBoth(Range2 other) {
    return create(xRange().includeBoth(other.xRange()), yRange().includeBoth(other.yRange()));
  }

  // Iterate through all (x,y) pairs.
  public void forEach(BiConsumer<Integer, Integer> callback) {
    yRange().stream().forEach(y -> xRange().stream().forEach(x -> callback.accept(x, y)));
  }
}
