package com.salisburyclan.lpviewport.layout;

import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.LayoutProvider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.List;

public class PickOneLayoutProvider implements LayoutProvider {

  private final static String TYPE = "pickone";
  private final static String DESCRIPTION =
    TYPE + " : Chooses one viewport of the specified devices";

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return ImmutableList.of(DESCRIPTION);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return TYPE.equals(layoutSpec);
  }

  @Override
  public Viewport createLayout(String layoutSpec, Collection<LaunchpadDevice> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for " + getClass().getName());
    }
    if (devices.isEmpty()) {
      throw new IllegalArgumentException("No devices connected");
    } else {
      // TODO: Interactively let user choose one of the available
      return Iterables.getFirst(devices, null).getViewport();
    }
  }
}
