package com.salisburyclan.launchpad.device.midi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.launchpad.testing.AssertThrows.assertThrows;

@RunWith(JUnit4.class)
public class ColorCodeTest {

  @Test
  public void testFromRGB() {
    assertThat(ColorCode.fromRGB(0,0,0)).isEqualTo(0x000000);
    assertThat(ColorCode.fromRGB(1,2,3)).isEqualTo(0x010203);
    assertThat(ColorCode.fromRGB(0x3f,0x3f,0x3f)).isEqualTo(0x3f3f3f);
  }

  @Test
  public void testFromRGBOutOfRange() {
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(0,0,-1));
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(0,-1,0));
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(-1,0,0));
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(0x40,0,0));
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(0,0x40,0));
    assertThrows(IllegalArgumentException.class, () -> ColorCode.fromRGB(0,0,0x40));
  }

  @Test
  public void getGet() {
    assertThat(ColorCode.getRed(0x000000)).isEqualTo(0);
    assertThat(ColorCode.getGreen(0x000000)).isEqualTo(0);
    assertThat(ColorCode.getBlue(0x000000)).isEqualTo(0);

    assertThat(ColorCode.getRed(0x010203)).isEqualTo(1);
    assertThat(ColorCode.getGreen(0x010203)).isEqualTo(2);
    assertThat(ColorCode.getBlue(0x010203)).isEqualTo(3);

    assertThat(ColorCode.getRed(0x3f3f3f)).isEqualTo(0x3f);
    assertThat(ColorCode.getGreen(0x3f3f3f)).isEqualTo(0x3f);
    assertThat(ColorCode.getBlue(0x3f3f3f)).isEqualTo(0x3f);
  }
}
