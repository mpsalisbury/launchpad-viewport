package com.salisburyclan.lpviewport.layer;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PixelTest {

  private Pixel gray(double intensity, double alpha) {
    return Pixel.create(DColor.create(intensity, intensity, intensity), alpha);
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
