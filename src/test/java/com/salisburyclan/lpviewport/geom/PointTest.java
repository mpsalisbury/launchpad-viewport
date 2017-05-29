package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PointTest {

  @Test
  public void testAddVector() {
    Point p = Point.create(3, 5);
    Vector v = Vector.create(1, 2);
    assertThat(p.add(v)).isEqualTo(Point.create(4, 7));
  }

  @Test
  public void testSubtractVector() {
    Point p = Point.create(3, 5);
    Vector v = Vector.create(1, 2);
    assertThat(p.subtract(v)).isEqualTo(Point.create(2, 3));
  }

  @Test
  public void testSubtractPoint() {
    Point p1 = Point.create(3, 5);
    Point p2 = Point.create(1, 2);
    assertThat(p1.subtract(p2)).isEqualTo(Vector.create(2, 3));
  }
}
