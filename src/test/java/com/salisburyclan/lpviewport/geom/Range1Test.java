package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

import com.google.common.truth.Truth8;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Range1Test {

  @Test
  public void testCreatesEmptyRange() {
    assertThat(Range1.create(4, 1).isEmpty()).isTrue();
    assertThat(Range1.create(4, 1)).isEqualTo(Range1.EMPTY);
  }

  @Test
  public void testMiddle() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.middle()).isEqualTo(5);
  }

  @Test
  public void testSize() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.size()).isEqualTo(7);
  }

  @Test
  public void testIsPointWithin() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.isPointWithin(2)).isTrue();
    assertThat(range.isPointWithin(5)).isTrue();
    assertThat(range.isPointWithin(8)).isTrue();
    assertThat(range.isPointWithin(1)).isFalse();
    assertThat(range.isPointWithin(-1)).isFalse();
    assertThat(range.isPointWithin(9)).isFalse();
    assertThat(range.isPointWithin(20)).isFalse();
  }

  @Test
  public void testAssertPointWithin() {
    Range1 range = Range1.create(2, 8);
    range.assertPointWithin(2);
    assertThrows(IllegalArgumentException.class, () -> range.assertPointWithin(20));
  }

  @Test
  public void testIsRangeWithin() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.isRangeWithin(Range1.create(2, 4))).isTrue();
    assertThat(range.isRangeWithin(Range1.create(2, 8))).isTrue();
    assertThat(range.isRangeWithin(Range1.create(3, 5))).isTrue();
    assertThat(range.isRangeWithin(Range1.create(5, 8))).isTrue();

    assertThat(range.isRangeWithin(Range1.create(0, 1))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(1, 2))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(1, 3))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(1, 8))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(1, 9))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(2, 9))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(5, 9))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(8, 9))).isFalse();
    assertThat(range.isRangeWithin(Range1.create(9, 10))).isFalse();
  }

  @Test
  public void testInset() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.inset(0, 0)).isEqualTo(Range1.create(2, 8));
    assertThat(range.inset(1, 1)).isEqualTo(Range1.create(3, 7));
    assertThat(range.inset(4, 2)).isEqualTo(Range1.create(6, 6));
    assertThat(range.inset(-1, -1)).isEqualTo(Range1.create(1, 9));
    assertThat(range.inset(4, 4)).isEqualTo(Range1.EMPTY);
  }

  @Test
  public void testShift() {
    Range1 range = Range1.create(2, 8);
    assertThat(range.shift(0)).isEqualTo(Range1.create(2, 8));
    assertThat(range.shift(1)).isEqualTo(Range1.create(3, 9));
    assertThat(range.shift(-1)).isEqualTo(Range1.create(1, 7));
  }

  @Test
  public void testUnion() {
    Range1 r13 = Range1.create(1, 3);
    Range1 r24 = Range1.create(2, 4);
    Range1 r35 = Range1.create(3, 5);
    Range1 r57 = Range1.create(5, 7);
    // adjacent
    assertThat(r13.union(r35)).isEqualTo(Range1.create(1, 5));
    assertThat(r35.union(r13)).isEqualTo(Range1.create(1, 5));
    // separate
    assertThat(r13.union(r57)).isEqualTo(Range1.create(1, 7));
    assertThat(r57.union(r13)).isEqualTo(Range1.create(1, 7));
    // overlapping
    assertThat(r13.union(r24)).isEqualTo(Range1.create(1, 4));
    assertThat(r24.union(r13)).isEqualTo(Range1.create(1, 4));
  }

  @Test
  public void testIntersect() {
    Range1 r13 = Range1.create(1, 3);
    Range1 r24 = Range1.create(2, 4);
    Range1 r35 = Range1.create(3, 5);
    Range1 r57 = Range1.create(5, 7);
    // adjacent
    assertThat(r13.intersect(r35)).isEqualTo(Range1.create(3, 3));
    assertThat(r35.intersect(r13)).isEqualTo(Range1.create(3, 3));
    // separate
    assertThat(r13.intersect(r57)).isEqualTo(Range1.create(1, 0));
    assertThat(r57.intersect(r13)).isEqualTo(Range1.create(1, 0));
    // overlapping
    assertThat(r13.intersect(r24)).isEqualTo(Range1.create(2, 3));
    assertThat(r24.intersect(r13)).isEqualTo(Range1.create(2, 3));
  }

  @Test
  public void testStream() {
    Truth8.assertThat(Range1.create(1, 4).stream().boxed()).containsExactly(1, 2, 3, 4).inOrder();
    Truth8.assertThat(Range1.create(1, 1).stream().boxed()).containsExactly(1).inOrder();
  }

  @Test
  public void testForEach() {
    Range1 range = Range1.create(3, 5);
    List<Integer> values = new ArrayList<>();
    range.forEach(x -> values.add(x));
    assertThat(values).containsExactly(3, 4, 5).inOrder();
  }
}
