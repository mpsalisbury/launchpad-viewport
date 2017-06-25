package com.salisburyclan.lpviewport.device.midi;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PositionColorCodeTest {

  @Test
  public void testPositionAndColor() {
    testPositionAndColor(1, 2, 0.0, 0.2, 1.0);
  }

  public void testPositionAndColor(int x, int y, double r, double g, double b) {
    int positionCode = PositionCode.fromXY(x, y);
    int colorCode = ColorCode.fromRGB(r, g, b);

    long positionColor = PositionColorCode.fromPositionAndColor(positionCode, colorCode);

    assertThat(PositionColorCode.getPosition(positionColor)).isEqualTo(positionCode);
    assertThat(PositionColorCode.getColor(positionColor)).isEqualTo(colorCode);
  }

  @Test
  public void testXYRGB() {
    testFromXYRGB(0, 0, 0.0, 0.0, 0.0, 0, 0, 0);
    testFromXYRGB(1, 2, 0.0, 0.5, 1.0, 0, 31, 63);
    testFromXYRGB(0x0f, 0x0f, 1.0, 1.0, 1.0, 63, 63, 63);
  }

  public void testFromXYRGB(
      int x, int y, double r, double g, double b, int expectedR, int expectedG, int expectedB) {
    long colorCode = PositionColorCode.fromXYRGB(x, y, r, g, b);
    assertThat(PositionColorCode.getX(colorCode)).isEqualTo(x);
    assertThat(PositionColorCode.getY(colorCode)).isEqualTo(y);
    assertThat(PositionColorCode.getRed(colorCode)).isEqualTo(expectedR);
    assertThat(PositionColorCode.getGreen(colorCode)).isEqualTo(expectedG);
    assertThat(PositionColorCode.getBlue(colorCode)).isEqualTo(expectedB);
  }

  @Test
  public void testFromXYRGBOutOfRange() {
    testFromXYRGBOutOfRange(0, 0, 0, 0, -1);
    testFromXYRGBOutOfRange(0, 0, 0, -1, 0);
    testFromXYRGBOutOfRange(0, 0, -1, 0, 0);
    testFromXYRGBOutOfRange(0, -1, 0, 0, 0);
    testFromXYRGBOutOfRange(-1, 0, 0, 0, 0);

    testFromXYRGBOutOfRange(0x10, 0, 0, 0, 0);
    testFromXYRGBOutOfRange(0, 0x10, 0, 0, 0);
    testFromXYRGBOutOfRange(0, 0, 1.1, 0, 0);
    testFromXYRGBOutOfRange(0, 0, 0, 1.1, 0);
    testFromXYRGBOutOfRange(0, 0, 0, 0, 1.1);
  }

  public void testFromXYRGBOutOfRange(int x, int y, double r, double g, double b) {
    assertThrows(IllegalArgumentException.class, () -> PositionColorCode.fromXYRGB(x, y, r, g, b));
  }
}
