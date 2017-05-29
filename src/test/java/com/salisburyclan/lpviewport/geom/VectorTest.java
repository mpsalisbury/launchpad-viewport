package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VectorTest {

  @Test
  public void testAddPoint() {
    Vector v = Vector.create(1, 2);
    Point p = Point.create(3, 5);
    assertThat(v.add(p)).isEqualTo(Point.create(4, 7));
  }

  @Test
  public void testAddVector() {
    Vector v1 = Vector.create(1, 2);
    Vector v2 = Vector.create(3, 5);
    assertThat(v1.add(v2)).isEqualTo(Vector.create(4, 7));
  }
}
