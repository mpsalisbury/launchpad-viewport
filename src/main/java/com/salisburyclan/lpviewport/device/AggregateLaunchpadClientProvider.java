package com.salisburyclan.lpviewport.device;

import com.google.common.collect.ImmutableSet;
import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.LaunchpadClientProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Combines LaunchpadClientProviders. */
public class AggregateLaunchpadClientProvider implements LaunchpadClientProvider {

  List<LaunchpadClientProvider> providers;

  public AggregateLaunchpadClientProvider() {
    providers = new ArrayList<>();
  }

  protected void addProvider(LaunchpadClientProvider provider) {
    providers.add(provider);
  }

  @Override
  public Set<String> getAvailableTypes() {
    return providers.stream()
        .map(LaunchpadClientProvider::getAvailableTypes)
	      .flatMap(Set::stream)
	      .collect(Collectors.toSet());
  }

  @Override
  public List<LaunchpadClient> getLaunchpadClients(Set<String> typeSpecs) {
    List<LaunchpadClient> clients = new ArrayList<>();
    providers.forEach((provider) -> {
      clients.addAll(provider.getLaunchpadClients(typeSpecs));
    });
    return clients;
  }
}
