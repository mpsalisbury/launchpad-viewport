package com.salisburyclan.lpviewport.device;

import com.google.common.collect.ImmutableSet;
import com.salisburyclan.lpviewport.api.Device;
import com.salisburyclan.lpviewport.api.DeviceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Combines DeviceProvider. */
public class AggregateDeviceProvider implements DeviceProvider {

  List<DeviceProvider> providers;

  public AggregateDeviceProvider() {
    providers = new ArrayList<>();
  }

  protected void addProvider(DeviceProvider provider) {
    providers.add(provider);
  }

  @Override
  public boolean supportsClientSpec(String clientSpec) {
    return providers.stream()
        .anyMatch(provider -> provider.supportsClientSpec(clientSpec));
  }

  @Override
  public Set<String> getAvailableTypes() {
    return providers.stream()
        .map(DeviceProvider::getAvailableTypes)
	      .flatMap(Set::stream)
	      .collect(Collectors.toSet());
  }

  @Override
  public List<LaunchpadClient> getDevices(Set<String> requestedTypeSpecs) {
    List<LaunchpadClient> clients = new ArrayList<>();
    providers.forEach((provider) -> {
      Set<String> supportedTypeSpecs = requestedTypeSpecs.stream()
          .filter(provider::supportsClientSpec)
          .collect(Collectors.toSet());
      if (!supportedTypeSpecs.isEmpty()) {
        clients.addAll(provider.getDevices(supportedTypeSpecs));
      }
    });
    return clients;
  }
}
