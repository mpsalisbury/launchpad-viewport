package com.salisburyclan.launchpad.api;

import java.util.stream.IntStream;

/**
 * Encodes a 1-D range.
 */
public class ViewStripExtent {
  private int low;
  private int high;

  // Builds a viewstripextent from range.
  public ViewStripExtent(int low, int high) {
    assertValidDimension(low, high);
    this.low = low;
    this.high = high;
  }

  // Validates that the given extentLow and extentHigh are valid values.
  private static void assertValidDimension(int extentLow, int extentHigh) {
    if (extentLow > extentHigh) {
      throw new IllegalArgumentException("Invalid extents: " + extentLow + "-" + extentHigh);
    }
  }

  public IntStream getRange() {
    return IntStream.rangeClosed(low, high);
  }

  public int getSize() {
    return high - low + 1;
  }

  public int getLow() {
    return low;
  }

  public int getHigh() {
    return high;
  }

  // Throws IllegalArgumentException if given point is not within this extent.
  public void assertPointWithin(int x) {
    if (!isPointWithin(x)) {
      throw new IllegalArgumentException(
          String.format("point(%s) out of extent range (%s, %s)", x, low, high));
    }
  }

  // Returns true iff given point is within this extent.
  public boolean isPointWithin(int x) {
    return (x >= low) && (x <= high);
  }
}
