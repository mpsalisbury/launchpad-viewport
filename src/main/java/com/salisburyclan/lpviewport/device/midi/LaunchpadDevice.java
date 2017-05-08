package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.ViewExtent;

/** Describes the protocol details of a particular Launchpad device. */
public interface LaunchpadDevice {
  /** Return the bytes to send at the beginning of a Sysex command. */
  byte[] getSysexPreamble();

  /** Return ViewExtent of the overall device, including side buttons. */
  ViewExtent getOverallExtent();

  /** Return ViewExtent of just the pads on the device. */
  ViewExtent getPadsExtent();

  /** Returns the protocol index for the given button Position. */
  byte posToIndex(int pos);

  /** Returns the protocol index for the given button position. */
  byte posToIndex(int x, int y);

  /** Returns the Position for the given protocol index. */
  int indexToPos(byte index);
}
