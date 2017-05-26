package com.salisburyclan.lpviewport.device.midi;

/** Encodes a 2-D x,y position in a single int. x and y must be in range 0..15. */
public class PositionCode {
  private PositionCode() {}

  // Builds a position from coordinates.
  public static int fromXY(int x, int y) {
    checkRange(x);
    checkRange(y);
    return (x << 8) | y;
  }

  // Extracts the X coordinate from a position.
  public static int getX(int pos) {
    return (pos >> 8) & 0x0f;
  }

  // Extracts the Y coordinate from a position.
  public static int getY(int pos) {
    return pos & 0x0f;
  }

  private static void checkRange(int coord) {
    if (coord < 0) {
      throw new IllegalArgumentException("Coord out of range: " + coord);
    }
    if (coord > 0x0f) {
      throw new IllegalArgumentException("Coord out of range: " + coord);
    }
  }
}
