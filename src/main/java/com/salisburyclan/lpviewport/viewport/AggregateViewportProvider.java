package com.salisburyclan.lpviewport.viewport;

import com.google.common.collect.ImmutableSet;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Combines ViewportProviders. */
public class AggregateViewportProvider implements ViewportProvider {

  List<ViewportProvider> providers;

  public AggregateViewportProvider() {
    providers = new ArrayList<>();
  }

  protected void addProvider(ViewportProvider provider) {
    providers.add(provider);
  }

  @Override
  public boolean supportsSpec(String viewportSpec) {
    return providers.stream()
        .anyMatch(provider -> provider.supportsSpec(viewportSpec));
  }

  @Override
  public List<String> getViewportSpecDescriptions() {
    return providers.stream()
        .map(ViewportProvider::getViewportSpecDescriptions)
	      .flatMap(List::stream)
	      .collect(Collectors.toList());
  }

  @Override
  public Viewport createViewport(String viewportSpec) {
    List<Viewport> viewports = new ArrayList<>();
    providers.forEach(provider -> {
      if (provider.supportsSpec(viewportSpec)) {
        viewports.add(provider.createViewport(viewportSpec));
      }
    });
    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("No provider match viewportSpec " + viewportSpec);
    }
    if (viewports.size() > 1) {
      throw new IllegalArgumentException("Multiple providers match viewportSpec " + viewportSpec);
    }
    return viewports.get(0);
  }
}
