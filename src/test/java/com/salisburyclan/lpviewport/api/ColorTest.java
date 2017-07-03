package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ColorTest {

  // Channel variation small enough to be considered equal.
  private static final double EPSILON = 0.000005;
  // Channel variation too big to be considered equal.
  private static final double DELTA = 0.0001;

  @Test
  public void testChannelOutOfRange() throws Exception {
    assertThrows(IllegalArgumentException.class, () -> Color.create(1.1, 0.0, 0.0));
    assertThrows(IllegalArgumentException.class, () -> Color.create(-0.1, 0.0, 0.0));
    assertThrows(IllegalArgumentException.class, () -> Color.create(0.0, 1.1, 0.0));
    assertThrows(IllegalArgumentException.class, () -> Color.create(0.0, -0.1, 0.0));
    assertThrows(IllegalArgumentException.class, () -> Color.create(0.0, 0.0, 1.1));
    assertThrows(IllegalArgumentException.class, () -> Color.create(0.0, 0.0, -0.1));
  }

  @Test
  public void testEquals() throws Exception {
    testEquals(0.0, 0.0, 0.0);
    testEquals(1.0, 0.0, 0.0);
    testEquals(0.0, 1.0, 0.0);
    testEquals(0.0, 0.0, 1.0);
    testEquals(0.1, 0.2, 0.3);
  }

  private void testEquals(double red, double green, double blue) {
    Color color = Color.create(red, green, blue);
    assertThat(color).isEqualTo(color);
    assertThat(color).isEqualTo(Color.create(red, green, blue));
    if (red < 1.0) {
      assertThat(color).isEqualTo(Color.create(red + EPSILON, green, blue));
      assertThat(color).isNotEqualTo(Color.create(red + DELTA, green, blue));
    }
    if (green < 1.0) {
      assertThat(color).isEqualTo(Color.create(red, green + EPSILON, blue));
      assertThat(color).isNotEqualTo(Color.create(red, green + DELTA, blue));
    }
    if (blue < 1.0) {
      assertThat(color).isEqualTo(Color.create(red, green, blue + EPSILON));
      assertThat(color).isNotEqualTo(Color.create(red, green, blue + DELTA));
    }
  }
}
