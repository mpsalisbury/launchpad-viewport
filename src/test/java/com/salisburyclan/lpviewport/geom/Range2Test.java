package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Range2Test {

  @Test
  public void testValidRange() {
    testValidRange(0, 0, 4, 4);
    testValidRange(1, 2, 1, 2);
  }

  private void testValidRange(int xLow, int yLow, int xHigh, int yHigh) {
    testValidRange(xLow, yLow, xHigh, yHigh, Range2.create(xLow, yLow, xHigh, yHigh));
    testValidRange(
        xLow,
        yLow,
        xHigh,
        yHigh,
        Range2.create(Point.create(xLow, yLow), Point.create(xHigh, yHigh)));
    testValidRange(
        xLow,
        yLow,
        xHigh,
        yHigh,
        Range2.create(Range1.create(xLow, xHigh), Range1.create(yLow, yHigh)));

    // 1-D ranges
    testValidRange(xLow, yLow, xLow, yHigh, Range2.create(xLow, Range1.create(yLow, yHigh)));
    testValidRange(xLow, yLow, xHigh, yLow, Range2.create(Range1.create(xLow, xHigh), yLow));
  }

  private void testValidRange(int xLow, int yLow, int xHigh, int yHigh, Range2 range) {
    assertThat(range.xRange()).isEqualTo(Range1.create(xLow, xHigh));
    assertThat(range.yRange()).isEqualTo(Range1.create(yLow, yHigh));
  }

  @Test
  public void testInvalidRange() {
    testInvalidRange(1, 1, 0, 1);
    testInvalidRange(1, 1, 1, 0);
  }

  private void testInvalidRange(int xLow, int yLow, int xHigh, int yHigh) {
    assertThrows(IllegalArgumentException.class, () -> Range2.create(xLow, yLow, xHigh, yHigh));
    assertThrows(
        IllegalArgumentException.class,
        () -> Range2.create(Point.create(xLow, yLow), Point.create(xHigh, yHigh)));
    assertThrows(
        IllegalArgumentException.class,
        () -> Range2.create(Range1.create(xLow, xHigh), Range1.create(yLow, yHigh)));
  }

  @Test
  public void testXRange() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.xRange()).isEqualTo(Range1.create(1, 4));
  }

  @Test
  public void testYRange() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.yRange()).isEqualTo(Range1.create(2, 7));
  }

  @Test
  public void testWidth() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.getWidth()).isEqualTo(4);
  }

  @Test
  public void testHeight() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.getHeight()).isEqualTo(6);
  }

  @Test
  public void testIsPointWithin() {
    Range2 range = Range2.create(1, 2, 4, 7);
    // Test corners, edges, and middles
    assertThat(range.isPointWithin(Point.create(1, 2))).isTrue();
    assertThat(range.isPointWithin(Point.create(2, 2))).isTrue();
    assertThat(range.isPointWithin(Point.create(4, 2))).isTrue();
    assertThat(range.isPointWithin(Point.create(1, 3))).isTrue();
    assertThat(range.isPointWithin(Point.create(2, 3))).isTrue();
    assertThat(range.isPointWithin(Point.create(4, 3))).isTrue();
    assertThat(range.isPointWithin(Point.create(1, 7))).isTrue();
    assertThat(range.isPointWithin(Point.create(2, 7))).isTrue();
    assertThat(range.isPointWithin(Point.create(4, 7))).isTrue();

    // Test just outside of valid ranges.
    assertThat(range.isPointWithin(Point.create(0, 1))).isFalse();
    assertThat(range.isPointWithin(Point.create(2, 1))).isFalse();
    assertThat(range.isPointWithin(Point.create(5, 1))).isFalse();
    assertThat(range.isPointWithin(Point.create(0, 2))).isFalse();
    assertThat(range.isPointWithin(Point.create(2, 2))).isTrue();
    assertThat(range.isPointWithin(Point.create(5, 2))).isFalse();
    assertThat(range.isPointWithin(Point.create(0, 8))).isFalse();
    assertThat(range.isPointWithin(Point.create(2, 8))).isFalse();
    assertThat(range.isPointWithin(Point.create(5, 8))).isFalse();
  }

  @Test
  public void testAssertPointWithin() {
    Range2 range = Range2.create(1, 2, 4, 7);
    range.assertPointWithin(Point.create(1, 2));
    assertThrows(IllegalArgumentException.class, () -> range.assertPointWithin(Point.create(0, 1)));
  }

  @Test
  public void testIsRangeWithin() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.isRangeWithin(Range2.create(1, 2, 4, 7))).isTrue();
    assertThat(range.isRangeWithin(Range2.create(2, 2, 4, 7))).isTrue();
    assertThat(range.isRangeWithin(Range2.create(1, 3, 4, 7))).isTrue();
    assertThat(range.isRangeWithin(Range2.create(1, 2, 3, 7))).isTrue();
    assertThat(range.isRangeWithin(Range2.create(1, 2, 4, 6))).isTrue();

    assertThat(range.isRangeWithin(Range2.create(0, 2, 4, 7))).isFalse();
    assertThat(range.isRangeWithin(Range2.create(1, 1, 4, 7))).isFalse();
    assertThat(range.isRangeWithin(Range2.create(1, 2, 5, 7))).isFalse();
    assertThat(range.isRangeWithin(Range2.create(1, 2, 4, 8))).isFalse();

    assertThat(range.isRangeWithin(Range2.create(5, 2, 6, 5))).isFalse();
  }

  @Test
  public void testInset() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.inset(0, 0, 0, 0)).isEqualTo(Range2.create(1, 2, 4, 7));
    assertThat(range.inset(1, 1, 1, 1)).isEqualTo(Range2.create(2, 3, 3, 6));
    assertThat(range.inset(1, 1, 2, 4)).isEqualTo(Range2.create(2, 3, 2, 3));
    assertThat(range.inset(-1, -1, -1, -1)).isEqualTo(Range2.create(0, 1, 5, 8));
    assertThrows(IllegalArgumentException.class, () -> range.inset(4, 0, 4, 0));
    assertThrows(IllegalArgumentException.class, () -> range.inset(0, 4, 0, 4));
  }

  @Test
  public void testShift() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.shift(Vector.create(0, 0))).isEqualTo(Range2.create(1, 2, 4, 7));
    assertThat(range.shift(Vector.create(1, 1))).isEqualTo(Range2.create(2, 3, 5, 8));
    assertThat(range.shift(Vector.create(1, -1))).isEqualTo(Range2.create(2, 1, 5, 6));
  }

  @Test
  public void testIncludeBoth() {
    // separate
    assertThat(Range2.create(1, 2, 3, 4).includeBoth(Range2.create(4, 5, 7, 8)))
        .isEqualTo(Range2.create(1, 2, 7, 8));
    assertThat(Range2.create(1, 5, 3, 8).includeBoth(Range2.create(4, 2, 7, 4)))
        .isEqualTo(Range2.create(1, 2, 7, 8));
    // contained
    assertThat(Range2.create(1, 2, 7, 8).includeBoth(Range2.create(2, 2, 5, 5)))
        .isEqualTo(Range2.create(1, 2, 7, 8));
    // overlapping
    assertThat(Range2.create(1, 2, 3, 4).includeBoth(Range2.create(2, 3, 5, 6)))
        .isEqualTo(Range2.create(1, 2, 5, 6));
  }
}