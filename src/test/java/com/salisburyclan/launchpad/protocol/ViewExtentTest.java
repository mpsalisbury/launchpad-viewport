package com.salisburyclan.launchpad.protocol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.launchpad.testing.AssertThrows.assertThrows;

@RunWith(JUnit4.class)
public class ViewExtentTest {

  @Test
  public void testExtent() {
    ViewExtent extent = new ViewExtent(0, 9, 2, 4);

    assertThat(extent.getWidth()).isEqualTo(10);
    assertThat(extent.getHeight()).isEqualTo(3);
    assertThat(extent.getXLow()).isEqualTo(0);
    assertThat(extent.getXHigh()).isEqualTo(9);
    assertThat(extent.getYLow()).isEqualTo(2);
    assertThat(extent.getYHigh()).isEqualTo(4);
  }

  @Test
  public void testCheckWithinSucceeds() {
    ViewExtent extent = new ViewExtent(0, 9, 2, 4);

    // None of these should throw.
    extent.checkWithin(0,2);
    extent.checkWithin(9,2);
    extent.checkWithin(9,4);
    extent.checkWithin(0,4);
    extent.checkWithin(2,3);
  }

  @Test
  public void testCheckWithinFails() {
    ViewExtent extent = new ViewExtent(0, 9, 2, 4);

    assertThrows(IllegalArgumentException.class, () -> extent.checkWithin(-1,2));
    assertThrows(IllegalArgumentException.class, () -> extent.checkWithin(11,2));
    assertThrows(IllegalArgumentException.class, () -> extent.checkWithin(2,0));
    assertThrows(IllegalArgumentException.class, () -> extent.checkWithin(2,5));
  }
}
