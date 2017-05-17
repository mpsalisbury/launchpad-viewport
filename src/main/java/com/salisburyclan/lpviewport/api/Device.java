package com.salisburyclan.lpviewport.api;

// A connection to a Launchpad device.
public interface Device {
  String getType();
  Viewport getViewport();
  void close();
}
