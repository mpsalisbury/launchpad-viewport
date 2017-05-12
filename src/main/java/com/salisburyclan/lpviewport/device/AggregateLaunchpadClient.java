package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.Viewport;

// A viewport that represents multiple viewports stitched together
public class AggregateLaunchpadClient implements LaunchpadClient {
  private LaunchpadClient leftClient;
  private LaunchpadClient rightClient;
  private Viewport viewport;

  public AggregateLaunchpadClient(LaunchpadClient leftClient, LaunchpadClient rightClient) {
    this.leftClient = leftClient;
    this.rightClient = rightClient;
    this.viewport = new AggregateViewport(leftClient.getViewport(), rightClient.getViewport());
  }

  @Override
  public String getType() {
    return String.format("%s.%s", leftClient.getType(), rightClient.getType());
  }

  @Override
  public Viewport getViewport() {
    return viewport;
  }

  @Override
  public void close() {
  }
}
