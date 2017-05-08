package com.salisburyclan.lpviewport.api;

import java.util.stream.IntStream;

/**
 * Encodes a 2-D xrange and yrange.
 */
public class ViewExtent {
  private int xLow;
  private int yLow;
  private int xHigh;
  private int yHigh;

  // Builds a viewextent from coordinates.
  public ViewExtent(int xLow, int yLow, int xHigh, int yHigh) {
    assertValidDimension(xLow, xHigh);
    assertValidDimension(yLow, yHigh);
    this.xLow = xLow;
    this.yLow = yLow;
    this.xHigh = xHigh;
    this.yHigh = yHigh;
  }

  // Validates that the given extentLow and extentHigh are valid values.
  private static void assertValidDimension(int extentLow, int extentHigh) {
    if (extentLow > extentHigh) {
      throw new IllegalArgumentException("Invalid extents: " + extentLow + "-" + extentHigh);
    }
  }

  public IntStream getXRange() {
    return IntStream.rangeClosed(xLow, xHigh);
  }

  public IntStream getYRange() {
    return IntStream.rangeClosed(yLow, yHigh);
  }

  public int getWidth() {
    return xHigh - xLow + 1;
  }

  public int getHeight() {
    return yHigh - yLow + 1;
  }

  public int getXLow() {
    return xLow;
  }

  public int getYLow() {
    return yLow;
  }

  public int getXHigh() {
    return xHigh;
  }

  public int getYHigh() {
    return yHigh;
  }

  // Throws IllegalArgumentException if given point is not within this extent.
  public void assertPointWithin(int x, int y) {
    if (!isPointWithin(x, y)) {
      throw new IllegalArgumentException(
          String.format("point(%s, %s) out of extent range (%s, %s, %s, %s)",
            x, y, xLow, yLow, xHigh, yHigh));
    }
  }

  // Returns true iff given point is within this extent.
  public boolean isPointWithin(int x, int y) {
    return (x >= xLow) && (x <= xHigh)
      && (y >= yLow) && (y <= yHigh);
  }

  // Returns true iff other extent is within this extent.
  public boolean isExtentWithin(ViewExtent other) {
    return isPointWithin(other.getXLow(), getYLow())
        && isPointWithin(other.getXHigh(), getYHigh());
  }
}
