package com.salisburyclan.lpviewport.layout;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.animation.Sweep;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
  public ListenableFuture<Viewport> createLayout(String layoutSpec, Collection<LaunchpadDevice> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for " + getClass().getName());
    }
    List<Viewport> viewports = devices.stream()
      .map(LaunchpadDevice::getViewport)
      .collect(Collectors.toList());

    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("Empty LayoutSpec: " + layoutSpec);
    } else if (viewports.size() == 1) {
      return Futures.immediateFuture(viewports.get(0));
    } else {
      return makeHorizontalViewport(devices.stream()
          .map(LaunchpadDevice::getViewport)
          .collect(Collectors.toList()));
    }
  }

  private ListenableFuture<Viewport> makeHorizontalViewport(List<Viewport> viewports) {
    return new ViewportBuilder(viewports).getFutureViewport();
  }

  private static class ViewportBuilder {
    private SettableFuture<Viewport> futureViewport;
    private AggregateViewport.Builder viewportBuilder;
    private Runnable teardownTemporaryViewport = null;
    // Leftmost X value for next added viewport.
    private int nextX = 0;
    private int remainingViewportCount = 0;

    public ViewportBuilder(List<Viewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.viewportBuilder = new AggregateViewport.Builder();
      viewports.forEach(viewport -> {
        setupViewport(viewport);
      });
    }

    private void setupViewport(Viewport viewport) {
      remainingViewportCount++;
      Animation animation = new Sweep(viewport, Color.RED);
      animation.play();
      viewport.addListener(new ViewportListener() {
        public void onButtonPressed(int x, int y) {
          animation.stop();
          viewport.setAllLights(Color.BLACK);
          viewport.removeListener(this);
          appendViewport(viewport);
        }
        public void onButtonReleased(int x, int y) {
        }
      });
    }

    // Add this viewport to the right of the previously chosen viewports.
    // If this is last one, return final viewport.
    private void appendViewport(Viewport viewport) {
      if (teardownTemporaryViewport != null) {
        teardownTemporaryViewport.run();
      }
      viewportBuilder.add(viewport, nextX, 0);
      nextX += viewport.getExtent().getWidth();
      remainingViewportCount--;

      if (remainingViewportCount > 0) {
        Viewport temporaryViewport = viewportBuilder.build();
        Animation animation = new Sweep(temporaryViewport, Color.BLUE);
        animation.play();
        teardownTemporaryViewport = (() -> {
          animation.stop();
          temporaryViewport.setAllLights(Color.BLACK);
        });
      } else {
        futureViewport.set(viewportBuilder.build());
      }
    }

    public ListenableFuture<Viewport> getFutureViewport() {
      return futureViewport;
    }
  }
}
