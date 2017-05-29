package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Assert;
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
    Range2.create(xLow, yLow, xHigh, yHigh);
    Range2.create(Point.create(xLow, yLow), Point.create(xHigh, yHigh));
    Range2.create(Range.create(xLow, xHigh), Range.create(yLow, yHigh));
  }

  @Test
  public void testInvalidRange() {
    testInvalidRange(1, 1, 0, 1);
    testInvalidRange(1, 1, 1, 0);
  }

  private void testInvalidRange(int xLow, int yLow, int xHigh, int yHigh) {
    assertInvalid(() -> Range2.create(xLow, yLow, xHigh, yHigh));
    assertInvalid(() -> Range2.create(Point.create(xLow, yLow), Point.create(xHigh, yHigh)));
    assertInvalid(() -> Range2.create(Range.create(xLow, xHigh), Range.create(yLow, yHigh)));
  }

  private void assertInvalid(Runnable throwingTest) {
    // TODO assertThrows form
    try {
      throwingTest.run();
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
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
    assertInvalid(() -> range.inset(4, 0, 4, 0));
    assertInvalid(() -> range.inset(0, 4, 0, 4));
  }

  @Test
  public void testShift() {
    Range2 range = Range2.create(1, 2, 4, 7);
    assertThat(range.shift(0, 0)).isEqualTo(Range2.create(1, 2, 4, 7));
    assertThat(range.shift(1, 1)).isEqualTo(Range2.create(2, 3, 5, 8));
    assertThat(range.shift(1, -1)).isEqualTo(Range2.create(2, 1, 5, 6));
  }

  /*
    // Returns a new ViewExtent that includes the full range of both this and other.
    public Range2 includeBoth(Range2 other) {
      return create(xRange().includeBoth(other.xRange()), yRange().includeBoth(other.yRange()));
    }
  @Test
  public void testIncludeBoth() {
    Range2 r13 = Range2.create(1, 3, 1, 3);
    Range2 r24 = Range2.create(2, 4, 2, 4);
    Range r24 = Range.create(2, 4);
    Range r35 = Range.create(3, 5);
    Range r57 = Range.create(5, 7);
    // adjacent
    assertThat(r13.includeBoth(r35)).isEqualTo(Range.create(1, 5));
    assertThat(r35.includeBoth(r13)).isEqualTo(Range.create(1, 5));
    // separate
    assertThat(r13.includeBoth(r57)).isEqualTo(Range.create(1, 7));
    assertThat(r57.includeBoth(r13)).isEqualTo(Range.create(1, 7));
    // overlapping
    assertThat(r13.includeBoth(r24)).isEqualTo(Range.create(1, 4));
    assertThat(r24.includeBoth(r13)).isEqualTo(Range.create(1, 4));
  }

  @Test
  public void testStream() {
    Truth8.assertThat(Range.create(1, 4).stream().boxed()).containsExactly(1, 2, 3, 4).inOrder();
    Truth8.assertThat(Range.create(1, 1).stream().boxed()).containsExactly(1).inOrder();
  }
  */

  /*
    // Throws IllegalArgumentException if given point is not within this extent.
    public void assertPointWithin(Point p) {
      if (!isPointWithin(p)) {
        throw new IllegalArgumentException(String.format("%s out of extent range %s", p, this));
      }
    }
  */
}
