package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.viewport.RawViewport;

// A connection to a Launchpad device.
public interface LaunchpadDevice {
  String getType();

  RawViewport getViewport();

  void close();
}
