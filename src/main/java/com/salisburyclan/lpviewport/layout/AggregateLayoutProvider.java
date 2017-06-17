package com.salisburyclan.lpviewport.layout;

import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.RawViewport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** Combines LayoutProviders. */
public class AggregateLayoutProvider implements LayoutProvider {

  List<LayoutProvider> providers;

  public AggregateLayoutProvider() {
    providers = new ArrayList<>();
  }

  protected void addProvider(LayoutProvider provider) {
    providers.add(provider);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return providers.stream().anyMatch(provider -> provider.supportsSpec(layoutSpec));
  }

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return providers
        .stream()
        .map(LayoutProvider::getLayoutSpecDescriptions)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public ListenableFuture<RawViewport> createLayout(
      String layoutSpec, Collection<LaunchpadDevice> devices) {
    List<LayoutProvider> supportingProviders =
        providers
            .stream()
            .filter(provider -> provider.supportsSpec(layoutSpec))
            .collect(Collectors.toList());
    if (supportingProviders.isEmpty()) {
      throw new IllegalArgumentException("No provider supports layoutSpec " + layoutSpec);
    }
    if (supportingProviders.size() > 1) {
      throw new IllegalArgumentException("Multiple providers support layoutSpec " + layoutSpec);
    }
    return supportingProviders.get(0).createLayout(layoutSpec, devices);
  }
}
