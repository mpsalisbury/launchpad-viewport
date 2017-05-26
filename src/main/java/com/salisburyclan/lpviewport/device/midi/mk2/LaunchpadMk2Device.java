package com.salisburyclan.lpviewport.device.midi.mk2;

import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.device.midi.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.midi.PositionCode;

public class LaunchpadMk2Device implements LaunchpadDevice {

  private static final byte[] sysexPreamble = {0x00, 0x20, 0x29, 0x02, 0x18};
  private static int minX = 0;
  private static int maxX = 8;
  private static int minY = 0;
  private static int maxY = 8;

  @Override
  public byte[] getSysexPreamble() {
    return sysexPreamble;
  }

  @Override
  public ViewExtent getOverallExtent() {
    return new ViewExtent(minX, minY, maxX, maxY);
  }

  @Override
  public ViewExtent getPadsExtent() {
    return new ViewExtent(0, 0, 7, 7);
  }

  @Override
  public byte posToIndex(int pos) {
    return posToIndex(PositionCode.getX(pos), PositionCode.getY(pos));
  }

  @Override
  public byte posToIndex(int x, int y) {
    if (x < minX || x > maxX) {
      throw new IllegalArgumentException("x position out of range: " + x);
    }
    if (y < minY || y > maxY) {
      throw new IllegalArgumentException("y position out of range: " + y);
    }
    if (y == 8) {
      return (byte) (104 + x);
    }
    return (byte) ((x + 1) + 10 * (y + 1));
  }

  @Override
  public int indexToPos(byte index) {
    if (index >= 104) {
      int x = index - 104;
      int y = 8;
      return PositionCode.fromXY(x, y);
    }
    int x = index % 10 - 1;
    int y = index / 10 - 1;
    return PositionCode.fromXY(x, y);
  }
}
