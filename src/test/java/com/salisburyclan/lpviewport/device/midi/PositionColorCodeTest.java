package com.salisburyclan.lpviewport.device.midi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;

@RunWith(JUnit4.class)
public class PositionColorCodeTest {

  @Test
  public void testPositionAndColor() {
    testPositionAndColor(1,2,3,4,5);
  }

  public void testPositionAndColor(int x, int y, int r, int g, int b) {
    int positionCode  = PositionCode.fromXY(x, y);
    int colorCode = ColorCode.fromRGB(r, g, b);

    long positionColor = PositionColorCode.fromPositionAndColor(positionCode, colorCode);

    assertThat(PositionColorCode.getPosition(positionColor)).isEqualTo(positionCode);
    assertThat(PositionColorCode.getColor(positionColor)).isEqualTo(colorCode);
  }

  @Test
  public void testXYRGB() {
    testFromXYRGB(0,0,0,0,0);
    testFromXYRGB(1,2,3,4,5);
    testFromXYRGB(0x0f, 0x0f, 0x3f, 0x3f, 0x3f);
  }

  public void testFromXYRGB(int x, int y, int r, int g, int b) {
    long colorCode = PositionColorCode.fromXYRGB(x, y, r, g, b);
    assertThat(PositionColorCode.getX(colorCode)).isEqualTo(x);
    assertThat(PositionColorCode.getY(colorCode)).isEqualTo(y);
    assertThat(PositionColorCode.getRed(colorCode)).isEqualTo(r);
    assertThat(PositionColorCode.getGreen(colorCode)).isEqualTo(g);
    assertThat(PositionColorCode.getBlue(colorCode)).isEqualTo(b);
  }

  @Test
  public void testFromXYRGBOutOfRange() {
    testFromXYRGBOutOfRange(0,0,0,0,-1);
    testFromXYRGBOutOfRange(0,0,0,-1,0);
    testFromXYRGBOutOfRange(0,0,-1,0,0);
    testFromXYRGBOutOfRange(0,-1,0,0,0);
    testFromXYRGBOutOfRange(-1,0,0,0,0);

    testFromXYRGBOutOfRange(0x10,0,0,0,0);
    testFromXYRGBOutOfRange(0,0x10,0,0,0);
    testFromXYRGBOutOfRange(0,0,0x40,0,0);
    testFromXYRGBOutOfRange(0,0,0,0x40,0);
    testFromXYRGBOutOfRange(0,0,0,0,0x40);
  }

  public void testFromXYRGBOutOfRange(int x, int y, int r, int g, int b) {
    assertThrows(IllegalArgumentException.class, () -> PositionColorCode.fromXYRGB(x,y,r,g,b));
  }
}
