package com.salisburyclan.lpviewport.viewport;

import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.LaunchpadDeviceProvider;
import com.salisburyclan.lpviewport.layout.LayoutProvider;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Composes a RawViewport given layout and device specs.
public class RawViewportConstructor {

  private LaunchpadDeviceProvider deviceProvider;
  private LayoutProvider layoutProvider;

  // @param deviceProvider provides access to all available devices
  // @param layoutProvider provides access to all available layouts
  public RawViewportConstructor(
      LaunchpadDeviceProvider deviceProvider, LayoutProvider layoutProvider) {
    this.deviceProvider = deviceProvider;
    this.layoutProvider = layoutProvider;
  }

  // Builds a viewport by passing all specified devices to the specified layout provider.
  public ListenableFuture<RawViewport> getRawViewport(String layoutSpec, String deviceSpec) {
    List<LaunchpadDevice> devices =
        Arrays.stream(deviceSpec.split(","))
            .flatMap(spec -> deviceProvider.getDevices(spec).stream())
            .collect(Collectors.toList());
    if (devices.isEmpty()) {
      throw new IllegalArgumentException("No Devices available for DeviceSpec: " + deviceSpec);
    }
    return layoutProvider.createLayout(layoutSpec, devices);
  }
}
