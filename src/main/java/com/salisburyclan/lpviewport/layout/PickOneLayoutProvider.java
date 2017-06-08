package com.salisburyclan.lpviewport.layout;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.animation.AnimationProvider;
import com.salisburyclan.lpviewport.animation.Spark;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PickOneLayoutProvider implements LayoutProvider {

  private static final String TYPE = "pickone";
  private static final String DESCRIPTION =
      TYPE + " : Chooses one viewport of the specified devices";
  private static final AnimationProvider AWAITING_SELECTION_ANIMATION =
      Sweep.newProvider(Color.RED, true);

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return ImmutableList.of(DESCRIPTION);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return TYPE.equals(layoutSpec);
  }

  @Override
  public ListenableFuture<Viewport> createLayout(
      String layoutSpec, Collection<LaunchpadDevice> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for " + getClass().getName());
    }
    if (devices.isEmpty()) {
      throw new IllegalArgumentException("No devices connected");
    } else if (devices.size() == 1) {
      return Futures.immediateFuture(Iterables.getOnlyElement(devices).getViewport());
    } else {
      return pickOneViewport(
          devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList()));
    }
  }

  private ListenableFuture<Viewport> pickOneViewport(List<Viewport> viewports) {
    return new ViewportChooser(viewports).getFutureViewport();
  }

  private static class ViewportChooser {
    private SettableFuture<Viewport> futureViewport;

    // Runnables that tear down the various Viewports (animations, listeners)
    private List<Runnable> tearDowners;

    public ViewportChooser(List<Viewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.tearDowners = new ArrayList<>();
      viewports.forEach(
          viewport -> {
            setupViewport(viewport);
          });
    }

    private void setupViewport(Viewport viewport) {
      Animation animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport);
      ViewportListener listener =
          new ViewportListener() {
            public void onButtonPressed(Point p) {
              shutDownChooser();
              new Spark(viewport, p, Color.BLUE).play();
              futureViewport.set(viewport);
            }

            public void onButtonReleased(Point p) {}
          };
      animation.play();
      viewport.addListener(listener);

      tearDowners.add(
          () -> {
            animation.stop();
            viewport.setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private synchronized void shutDownChooser() {
      tearDowners.forEach(Runnable::run);
      tearDowners.clear();
    }

    public ListenableFuture<Viewport> getFutureViewport() {
      return futureViewport;
    }
  }
}
