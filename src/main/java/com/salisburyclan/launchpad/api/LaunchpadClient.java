package com.salisburyclan.launchpad.api;

public interface LaunchpadClient {
  String getType();
  Viewport getViewport();
  void close();
}
