package com.salisburyclan.lpviewport.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Combines DeviceProvider. */
public class AggregateDeviceProvider implements LaunchpadDeviceProvider {

  List<LaunchpadDeviceProvider> providers;

  public AggregateDeviceProvider() {
    providers = new ArrayList<>();
  }

  protected void addProvider(LaunchpadDeviceProvider provider) {
    providers.add(provider);
  }

  @Override
  public boolean supportsDeviceSpec(String deviceSpec) {
    return providers.stream().anyMatch(provider -> provider.supportsDeviceSpec(deviceSpec));
  }

  @Override
  public Set<String> getAvailableTypes() {
    return providers
        .stream()
        .map(LaunchpadDeviceProvider::getAvailableTypes)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public List<LaunchpadDevice> getDevices(String requestedDeviceSpec) {
    List<LaunchpadDevice> devices = new ArrayList<>();
    providers.forEach(
        provider -> {
          if (provider.supportsDeviceSpec(requestedDeviceSpec)) {
            devices.addAll(provider.getDevices(requestedDeviceSpec));
          }
        });
    return devices;
  }
}
