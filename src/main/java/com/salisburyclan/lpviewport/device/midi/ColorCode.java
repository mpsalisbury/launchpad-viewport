package com.salisburyclan.lpviewport.device.midi;

/**
 * Encodes an rgb color in a single int.
 * r, g, and b must be in range 0..63.
 */
public class ColorCode {
  private ColorCode() {}

  // Builds a color from components.
  public static int fromRGB(int r, int g, int b) {
    checkRange(r);
    checkRange(g);
    checkRange(b);
    return (((r << 8) | g) << 8) | b;
  }

  // Extracts the R component from a color.
  public static byte getRed(int color) {
    return (byte)((color >> 16) & 0x3f);
  }

  // Extracts the G component from a color.
  public static byte getGreen(int color) {
    return (byte)((color >> 8) & 0x3f);
  }

  // Extracts the B component from a color.
  public static byte getBlue(int color) {
    return (byte)(color & 0x3f);
  }

  private static void checkRange(int component) {
    if (component < 0) {
      throw new IllegalArgumentException("Component out of range: " + component);
    }
    if (component > 0x3f) {
      throw new IllegalArgumentException("Component out of range: " + component);
    }
  }
}
