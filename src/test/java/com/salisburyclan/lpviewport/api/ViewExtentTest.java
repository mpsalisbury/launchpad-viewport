package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ViewExtentTest {

  @Test
  public void testExtent() {
    ViewExtent extent = new ViewExtent(0, 2, 9, 4);

    assertThat(extent.getWidth()).isEqualTo(10);
    assertThat(extent.getHeight()).isEqualTo(3);
    assertThat(extent.getXLow()).isEqualTo(0);
    assertThat(extent.getXHigh()).isEqualTo(9);
    assertThat(extent.getYLow()).isEqualTo(2);
    assertThat(extent.getYHigh()).isEqualTo(4);
  }

  @Test
  public void testIsPointWithin() {
    ViewExtent extent = new ViewExtent(0, 2, 9, 4);

    assertThat(extent.isPointWithin(0, 2)).isEqualTo(true);
    assertThat(extent.isPointWithin(9, 2)).isEqualTo(true);
    assertThat(extent.isPointWithin(9, 4)).isEqualTo(true);
    assertThat(extent.isPointWithin(0, 4)).isEqualTo(true);
    assertThat(extent.isPointWithin(2, 3)).isEqualTo(true);

    assertThat(extent.isPointWithin(-1, 2)).isEqualTo(false);
    assertThat(extent.isPointWithin(11, 2)).isEqualTo(false);
    assertThat(extent.isPointWithin(2, 0)).isEqualTo(false);
    assertThat(extent.isPointWithin(2, 5)).isEqualTo(false);
  }
}
