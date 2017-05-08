package com.salisburyclan.lpviewport.device.midi;

/**
 * Encodes an xy position and rgb color in a single long.
 * x and y must be in range 0..15.
 * r, g, and b must be in range 0..63.
 */
public class PositionColorCode {
  private PositionColorCode() {}

  // Builds a positioncolor from components.
  public static long fromXYRGB(int x, int y, int r, int g, int b) {
    int position = PositionCode.fromXY(x, y);
    int color = ColorCode.fromRGB(r, g, b);
    return fromPositionAndColor(position, color);
  }

  // Builds a positioncolor from position and color.
  public static long fromPositionAndColor(int position, int color) {
    return ((long)position << 32) | color;
  }

  // Extracts the Position from a positioncolor.
  public static int getPosition(long poscolor) {
    return (int)((poscolor >> 32) & 0xffff);
  }

  // Extracts the Color from a positioncolor.
  public static int getColor(long poscolor) {
    return (int)(poscolor & 0xffffff);
  }

  // Extracts the X coordinate from a positioncolor.
  public static int getX(long poscolor) {
    return PositionCode.getX(getPosition(poscolor));
  }

  // Extracts the Y coordinate from a positioncolor.
  public static int getY(long poscolor) {
    return PositionCode.getY(getPosition(poscolor));
  }

  // Extracts the Red component from a positioncolor.
  public static byte getRed(long poscolor) {
    return ColorCode.getRed(getColor(poscolor));
  }

  // Extracts the Green component from a positioncolor.
  public static byte getGreen(long poscolor) {
    return ColorCode.getGreen(getColor(poscolor));
  }

  // Extracts the Blue component from a positioncolor.
  public static byte getBlue(long poscolor) {
    return ColorCode.getBlue(getColor(poscolor));
  }
}
