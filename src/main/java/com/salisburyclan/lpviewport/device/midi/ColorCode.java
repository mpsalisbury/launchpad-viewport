package com.salisburyclan.lpviewport.device.midi;

/** Encodes an rgb color in a single int. r, g, and b must be in range 0..1. */
public class ColorCode {
  private ColorCode() {}

  private static final int MAX_VALUE = 63;

  // Builds a color from components.
  public static int fromRGB(double r, double g, double b) {
    checkRange(r);
    checkRange(g);
    checkRange(b);
    return (((fromDouble(r) << 8) | fromDouble(g)) << 8) | fromDouble(b);
  }

  // Extracts the R component from a color.
  public static byte getRed(int color) {
    return (byte) ((color >> 16) & 0x3f);
  }

  // Extracts the G component from a color.
  public static byte getGreen(int color) {
    return (byte) ((color >> 8) & 0x3f);
  }

  // Extracts the B component from a color.
  public static byte getBlue(int color) {
    return (byte) (color & 0x3f);
  }

  private static int fromDouble(double value) {
    return (int) (value * MAX_VALUE);
  }

  private static void checkRange(double component) {
    if (component < 0.0) {
      throw new IllegalArgumentException("Color Component out of range: " + component);
    }
    if (component > 1.0) {
      throw new IllegalArgumentException("Color Component out of range: " + component);
    }
  }
}
