package com.salisburyclan.lpviewport.layout.linked;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.RawViewport;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LinkedPairsLayoutProvider implements LayoutProvider {

  private static final String TYPE = "linked";
  private static final String DESCRIPTION =
      TYPE + " : combines the viewports by user selecting attachment points";

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return ImmutableList.of(DESCRIPTION);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return TYPE.equals(layoutSpec);
  }

  @Override
  public ListenableFuture<RawViewport> createLayout(
      String layoutSpec, Collection<LaunchpadDevice> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for " + getClass().getName());
    }
    List<RawViewport> viewports =
        devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList());

    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("Empty LayoutSpec: " + layoutSpec);
    } else if (viewports.size() == 1) {
      return Futures.immediateFuture(viewports.get(0));
    } else {
      return new LinkedViewportBuilder(viewports).getFutureViewport();
    }
  }
}
