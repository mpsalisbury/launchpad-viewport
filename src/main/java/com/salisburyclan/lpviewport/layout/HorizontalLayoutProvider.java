package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.LayoutProvider;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class HorizontalLayoutProvider implements LayoutProvider {

  private final static String TYPE = "horiz";
  private final static String DESCRIPTION =
    TYPE + " : combines the viewports specified by typespec horizontally left-to-right";

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return ImmutableList.of(DESCRIPTION);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return TYPE.equals(layoutSpec);
  }

  @Override
  public Viewport createLayout(String layoutSpec, Collection<Device> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for HorizontalViewportProvider");
    }
    List<Viewport> viewports = Arrays.stream(devices)
      .map(Device::getViewport)
      .collect(Collectors.toList());

    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("Empty TypeSpec: " + typeSpec);
    } else if (viewports.size() == 1) {
      return viewports.get(0);
    } else {
      AggregateViewport.Builder builder = new AggregateViewport.Builder();
      int nextX = 0;
      for (Viewport viewport : viewports) {
        builder.add(viewport, nextX, 0);
        nextX += viewport.getExtent().getWidth();
      }
      return builder.build();
    }
  }
}
