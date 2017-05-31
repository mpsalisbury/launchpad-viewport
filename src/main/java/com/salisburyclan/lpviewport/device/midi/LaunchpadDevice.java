package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.geom.Range2;

/** Describes the protocol details of a particular Launchpad device. */
public interface LaunchpadDevice {
  /** Return the bytes to send at the beginning of a Sysex command. */
  byte[] getSysexPreamble();

  /** Return extent of the overall device, including side buttons. */
  Range2 getOverallExtent();

  /** Return extent of just the pads on the device. */
  Range2 getPadsExtent();

  /** Returns the protocol index for the given button Position. */
  byte posToIndex(int pos);

  /** Returns the protocol index for the given button position. */
  byte posToIndex(int x, int y);

  /** Returns the Position for the given protocol index. */
  int indexToPos(byte index);
}
