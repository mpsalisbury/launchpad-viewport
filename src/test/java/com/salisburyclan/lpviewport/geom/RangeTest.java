package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.truth.Truth8;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RangeTest {

  @Test
  public void testRequireValidRange() {
    Range.create(1, 4);
    Range.create(1, 1);
    // TODO assertThrows form
    try {
      Range.create(1, 0);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testSize() {
    Range range = Range.create(2, 8);
    assertThat(range.size()).isEqualTo(7);
  }

  @Test
  public void testIsPointWithin() {
    Range range = Range.create(2, 8);
    assertThat(range.isPointWithin(2)).isTrue();
    assertThat(range.isPointWithin(5)).isTrue();
    assertThat(range.isPointWithin(8)).isTrue();
    assertThat(range.isPointWithin(1)).isFalse();
    assertThat(range.isPointWithin(-1)).isFalse();
    assertThat(range.isPointWithin(9)).isFalse();
    assertThat(range.isPointWithin(20)).isFalse();
  }

  @Test
  public void testIsRangeWithin() {
    Range range = Range.create(2, 8);
    assertThat(range.isRangeWithin(Range.create(2, 4))).isTrue();
    assertThat(range.isRangeWithin(Range.create(2, 8))).isTrue();
    assertThat(range.isRangeWithin(Range.create(3, 5))).isTrue();
    assertThat(range.isRangeWithin(Range.create(5, 8))).isTrue();

    assertThat(range.isRangeWithin(Range.create(0, 1))).isFalse();
    assertThat(range.isRangeWithin(Range.create(1, 2))).isFalse();
    assertThat(range.isRangeWithin(Range.create(1, 3))).isFalse();
    assertThat(range.isRangeWithin(Range.create(1, 8))).isFalse();
    assertThat(range.isRangeWithin(Range.create(1, 9))).isFalse();
    assertThat(range.isRangeWithin(Range.create(2, 9))).isFalse();
    assertThat(range.isRangeWithin(Range.create(5, 9))).isFalse();
    assertThat(range.isRangeWithin(Range.create(8, 9))).isFalse();
    assertThat(range.isRangeWithin(Range.create(9, 10))).isFalse();
  }

  @Test
  public void testInset() {
    Range range = Range.create(2, 8);
    assertThat(range.inset(0, 0)).isEqualTo(Range.create(2, 8));
    assertThat(range.inset(1, 1)).isEqualTo(Range.create(3, 7));
    assertThat(range.inset(4, 2)).isEqualTo(Range.create(6, 6));
    assertThat(range.inset(-1, -1)).isEqualTo(Range.create(1, 9));
    try {
      // TODO: assertThrows form
      range.inset(4, 4);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testShift() {
    Range range = Range.create(2, 8);
    assertThat(range.shift(0)).isEqualTo(Range.create(2, 8));
    assertThat(range.shift(1)).isEqualTo(Range.create(3, 9));
    assertThat(range.shift(-1)).isEqualTo(Range.create(1, 7));
  }

  @Test
  public void testIncludeBoth() {
    Range r13 = Range.create(1, 3);
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
}
