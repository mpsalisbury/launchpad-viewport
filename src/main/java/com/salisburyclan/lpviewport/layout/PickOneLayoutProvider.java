package com.salisburyclan.lpviewport.layout;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.AnimationProvider;
import com.salisburyclan.lpviewport.animation.FramedAnimation;
import com.salisburyclan.lpviewport.animation.Spark2;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.layer.AnimatedLayer;
import com.salisburyclan.lpviewport.layer.AnimatedLayerPlayer;
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
  public ListenableFuture<RawViewport> createLayout(
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

  private ListenableFuture<RawViewport> pickOneViewport(List<RawViewport> viewports) {
    return new ViewportChooser(viewports).getFutureViewport();
  }

  private static class ViewportChooser {
    private SettableFuture<RawViewport> futureViewport;

    // Runnables that tear down the various Viewports (animations, listeners)
    private List<Runnable> tearDowners;

    public ViewportChooser(List<RawViewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.tearDowners = new ArrayList<>();
      viewports.forEach(
          viewport -> {
            setupViewport(viewport);
          });
    }

    private void setupViewport(RawViewport viewport) {
      FramedAnimation animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport.getExtent());
      Button2Listener listener =
          new Button2Listener() {
            public void onButtonPressed(Point p) {
              shutDownChooser();
              Spark2.play(viewport, p, Color.BLUE);
              futureViewport.set(viewport);
            }

            public void onButtonReleased(Point p) {}
          };
      AnimatedLayerPlayer.play(animation, viewport);
      viewport.addListener(listener);

      tearDowners.add(
          () -> {
            animation.stop();
            viewport.getLightLayer().setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private synchronized void shutDownChooser() {
      tearDowners.forEach(Runnable::run);
      tearDowners.clear();
    }

    public ListenableFuture<RawViewport> getFutureViewport() {
      return futureViewport;
    }
  }
}
