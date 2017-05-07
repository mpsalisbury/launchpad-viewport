package com.salisburyclan.launchpad.device.midi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.launchpad.testing.AssertThrows.assertThrows;

@RunWith(JUnit4.class)
public class PositionCodeTest {

  @Test
  public void testFromXY() {
    assertThat(PositionCode.fromXY(0,0)).isEqualTo(0x0000);
    assertThat(PositionCode.fromXY(1,2)).isEqualTo(0x0102);
    assertThat(PositionCode.fromXY(0x0f,0x0f)).isEqualTo(0x0f0f);
  }

  @Test
  public void testFromXYOutOfRange() {
    assertThrows(IllegalArgumentException.class, () -> PositionCode.fromXY(-1,0));
    assertThrows(IllegalArgumentException.class, () -> PositionCode.fromXY(0,-1));
    assertThrows(IllegalArgumentException.class, () -> PositionCode.fromXY(0x10,0));
    assertThrows(IllegalArgumentException.class, () -> PositionCode.fromXY(0,0x10));
  }

  @Test
  public void getGet() {
    assertThat(PositionCode.getX(0x0000)).isEqualTo(0);
    assertThat(PositionCode.getY(0x0000)).isEqualTo(0);

    assertThat(PositionCode.getX(0x0102)).isEqualTo(1);
    assertThat(PositionCode.getY(0x0102)).isEqualTo(2);

    assertThat(PositionCode.getX(0x0f0f)).isEqualTo(0x0f);
    assertThat(PositionCode.getY(0x0f0f)).isEqualTo(0x0f);
  }
}
