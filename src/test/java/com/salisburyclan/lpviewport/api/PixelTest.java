package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PixelTest {

  // Alpha variation small enough to be considered equal.
  private static final double EPSILON = 0.000005;
  // Alpha variation too big to be considered equal.
  private static final double DELTA = 0.0001;

  private static final Color COLOR = Color.create(0.1, 0.2, 0.3);

  @Test
  public void testAlphaOutOfRange() throws Exception {
    assertThrows(IllegalArgumentException.class, () -> Pixel.create(COLOR, 1.1));
    assertThrows(IllegalArgumentException.class, () -> Pixel.create(COLOR, -0.1));
  }

  @Test
  public void testEquals() throws Exception {
    testEquals(0.0);
    testEquals(0.1);
    testEquals(1.0);
  }

  private void testEquals(double alpha) {
    Pixel pixel = Pixel.create(COLOR, alpha);
    assertThat(pixel).isEqualTo(pixel);
    assertThat(pixel).isEqualTo(Pixel.create(COLOR, alpha));
    if (alpha < 1.0) {
      assertThat(pixel).isEqualTo(Pixel.create(COLOR, alpha + EPSILON));
      assertThat(pixel).isNotEqualTo(Pixel.create(COLOR, alpha + DELTA));
    }
  }

  private Pixel gray(double intensity, double alpha) {
    return Pixel.create(Color.create(intensity, intensity, intensity), alpha);
  }

  @Test
  public void testCombineOpaqueOver() throws Exception {
    assertThat(gray(0.0, 1.0).combine(gray(0.0, 1.0))).isEqualTo(gray(0.0, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(0.0, 1.0))).isEqualTo(gray(0.0, 1.0));
    assertThat(gray(0.0, 1.0).combine(gray(1.0, 1.0))).isEqualTo(gray(1.0, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(1.0, 1.0))).isEqualTo(gray(1.0, 1.0));
    assertThat(gray(0.0, 1.0).combine(gray(0.6, 1.0))).isEqualTo(gray(0.6, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(0.6, 1.0))).isEqualTo(gray(0.6, 1.0));
    assertThat(gray(0.4, 0.3).combine(gray(0.6, 1.0))).isEqualTo(gray(0.6, 1.0));
    assertThat(gray(0.4, 0.0).combine(gray(0.6, 1.0))).isEqualTo(gray(0.6, 1.0));
  }

  @Test
  public void testCombineTransparentOver() throws Exception {
    assertThat(gray(0.0, 1.0).combine(gray(0.0, 0.0))).isEqualTo(gray(0.0, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(0.0, 0.0))).isEqualTo(gray(1.0, 1.0));
    assertThat(gray(0.0, 0.3).combine(gray(1.0, 0.0))).isEqualTo(gray(0.0, 0.3));
    assertThat(gray(1.0, 0.3).combine(gray(1.0, 0.0))).isEqualTo(gray(1.0, 0.3));
    assertThat(gray(0.4, 0.3).combine(gray(0.6, 0.0))).isEqualTo(gray(0.4, 0.3));
    assertThat(gray(1.0, 0.0).combine(gray(0.6, 0.0))).isEqualTo(gray(0.0, 0.0));
  }

  @Test
  public void testCombineOpaqueUnder() throws Exception {
    assertThat(gray(0.0, 1.0).combine(gray(0.0, 0.3))).isEqualTo(gray(0.0, 1.0));
    assertThat(gray(0.0, 1.0).combine(gray(0.0, 0.7))).isEqualTo(gray(0.0, 1.0));
    assertThat(gray(0.0, 1.0).combine(gray(1.0, 0.3))).isEqualTo(gray(0.3, 1.0));
    assertThat(gray(0.0, 1.0).combine(gray(1.0, 0.7))).isEqualTo(gray(0.7, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(0.0, 0.3))).isEqualTo(gray(0.7, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(0.0, 0.7))).isEqualTo(gray(0.3, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(1.0, 0.3))).isEqualTo(gray(1.0, 1.0));
    assertThat(gray(1.0, 1.0).combine(gray(1.0, 0.7))).isEqualTo(gray(1.0, 1.0));
  }

  @Test
  public void testCombineSemiOpaqueUnder() throws Exception {
    assertThat(gray(0.0, 0.3).combine(gray(0.0, 0.3))).isEqualTo(gray(0.0, 0.51));
    assertThat(gray(0.0, 0.3).combine(gray(0.0, 0.7))).isEqualTo(gray(0.0, 0.79));
    assertThat(gray(0.0, 0.3).combine(gray(1.0, 0.3))).isEqualTo(gray(0.5882352, 0.51));
    assertThat(gray(0.0, 0.3).combine(gray(1.0, 0.7))).isEqualTo(gray(0.8860759, 0.79));
    assertThat(gray(1.0, 0.3).combine(gray(0.0, 0.3))).isEqualTo(gray(0.4117647, 0.51));
    assertThat(gray(1.0, 0.3).combine(gray(0.0, 0.7))).isEqualTo(gray(0.1139240, 0.79));
    assertThat(gray(1.0, 0.3).combine(gray(1.0, 0.3))).isEqualTo(gray(1.0, 0.51));
    assertThat(gray(1.0, 0.3).combine(gray(1.0, 0.7))).isEqualTo(gray(1.0, 0.79));
  }
}
