package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EdgeTest {

  @Test
  public void testOpposite() {
    assertThat(Edge.LEFT.getOpposite()).isEqualTo(Edge.RIGHT);
    assertThat(Edge.RIGHT.getOpposite()).isEqualTo(Edge.LEFT);
    assertThat(Edge.TOP.getOpposite()).isEqualTo(Edge.BOTTOM);
    assertThat(Edge.BOTTOM.getOpposite()).isEqualTo(Edge.TOP);
    assertThat(Edge.INVALID.getOpposite()).isEqualTo(Edge.INVALID);
  }

  @Test
  public void testGetRange() {
    Range2 extent = Range2.create(1, 2, 3, 4);
    assertThat(Edge.LEFT.getRange(extent)).isEqualTo(Range1.create(2, 4));
    assertThat(Edge.RIGHT.getRange(extent)).isEqualTo(Range1.create(2, 4));
    assertThat(Edge.TOP.getRange(extent)).isEqualTo(Range1.create(1, 3));
    assertThat(Edge.BOTTOM.getRange(extent)).isEqualTo(Range1.create(1, 3));
  }

  @Test
  public void testGetPoint() {
    Range2 extent = Range2.create(1, 2, 3, 4);
    assertThat(Edge.LEFT.getPoint(extent, 2)).isEqualTo(Point.create(1, 2));
    assertThat(Edge.LEFT.getPoint(extent, 4)).isEqualTo(Point.create(1, 4));
    assertThat(Edge.RIGHT.getPoint(extent, 2)).isEqualTo(Point.create(3, 2));
    assertThat(Edge.TOP.getPoint(extent, 2)).isEqualTo(Point.create(2, 4));
    assertThat(Edge.BOTTOM.getPoint(extent, 2)).isEqualTo(Point.create(2, 2));
  }

  @Test
  public void testIsEdge() {
    Range2 extent = Range2.create(1, 2, 3, 4);
    assertThat(Edge.LEFT.isEdge(extent, Point.create(1, 1))).isFalse();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(1, 2))).isTrue();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(1, 3))).isTrue();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(1, 4))).isTrue();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(1, 5))).isFalse();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(2, 3))).isFalse();
    assertThat(Edge.LEFT.isEdge(extent, Point.create(0, 3))).isFalse();

    assertThat(Edge.RIGHT.isEdge(extent, Point.create(3, 1))).isFalse();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(3, 2))).isTrue();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(3, 3))).isTrue();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(3, 4))).isTrue();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(3, 5))).isFalse();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(2, 3))).isFalse();
    assertThat(Edge.RIGHT.isEdge(extent, Point.create(4, 3))).isFalse();

    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(0, 2))).isFalse();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(1, 2))).isTrue();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(2, 2))).isTrue();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(3, 2))).isTrue();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(4, 2))).isFalse();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(3, 1))).isFalse();
    assertThat(Edge.BOTTOM.isEdge(extent, Point.create(3, 3))).isFalse();

    assertThat(Edge.TOP.isEdge(extent, Point.create(0, 4))).isFalse();
    assertThat(Edge.TOP.isEdge(extent, Point.create(1, 4))).isTrue();
    assertThat(Edge.TOP.isEdge(extent, Point.create(2, 4))).isTrue();
    assertThat(Edge.TOP.isEdge(extent, Point.create(3, 4))).isTrue();
    assertThat(Edge.TOP.isEdge(extent, Point.create(4, 4))).isFalse();
    assertThat(Edge.TOP.isEdge(extent, Point.create(3, 3))).isFalse();
    assertThat(Edge.TOP.isEdge(extent, Point.create(3, 5))).isFalse();
  }

  @Test
  public void testGetEdge() {
    Range2 extent = Range2.create(1, 2, 3, 4);
    assertThat(Edge.getEdge(extent, Point.create(1, 2))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(2, 2))).isEqualTo(Edge.BOTTOM);
    assertThat(Edge.getEdge(extent, Point.create(3, 2))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(0, 3))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(1, 3))).isEqualTo(Edge.LEFT);
    assertThat(Edge.getEdge(extent, Point.create(2, 3))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(3, 3))).isEqualTo(Edge.RIGHT);
    assertThat(Edge.getEdge(extent, Point.create(4, 3))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(1, 4))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(2, 4))).isEqualTo(Edge.TOP);
    assertThat(Edge.getEdge(extent, Point.create(3, 4))).isEqualTo(Edge.INVALID);
    assertThat(Edge.getEdge(extent, Point.create(4, 3))).isEqualTo(Edge.INVALID);
  }
}
