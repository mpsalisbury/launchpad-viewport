package com.salisburyclan.lpviewport.api;

public interface LaunchpadClient {
  String getType();
  Viewport getViewport();
  void close();
}
