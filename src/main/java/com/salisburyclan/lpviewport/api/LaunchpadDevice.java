package com.salisburyclan.lpviewport.api;

// A connection to a Launchpad device.
public interface LaunchpadDevice {
  String getType();

  RawViewport getViewport();

  void close();
}
