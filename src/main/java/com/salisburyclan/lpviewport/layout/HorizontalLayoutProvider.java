package com.salisburyclan.lpviewport.layout;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.Spark;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.AnimationProvider;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.viewport.AnimatedLayerPlayer;
import com.salisburyclan.lpviewport.viewport.RawViewport;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HorizontalLayoutProvider implements LayoutProvider {

  private static final String TYPE = "horiz";
  private static final String DESCRIPTION =
      TYPE + " : combines the viewports specified by typespec horizontally left-to-right";
  private static final AnimationProvider AWAITING_SELECTION_ANIMATION =
      Sweep.newProvider(Color.RED, true);
  private static final AnimationProvider SELECTED_VIEWPORT_ANIMATION =
      Sweep.newProvider(Color.BLUE, true);

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
      return makeHorizontalViewport(
          devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList()));
    }
  }

  private ListenableFuture<RawViewport> makeHorizontalViewport(List<RawViewport> viewports) {
    return new ViewportBuilder(viewports).getFutureViewport();
  }

  private static class ViewportBuilder {
    private SettableFuture<RawViewport> futureViewport;
    private AggregateViewport.Builder viewportBuilder;
    private Runnable teardownTemporaryViewport = null;
    // Leftmost X value for next added viewport.
    private int nextX = 0;
    private int remainingViewportCount = 0;

    public ViewportBuilder(List<RawViewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.viewportBuilder = new AggregateViewport.Builder();
      viewports.forEach(
          viewport -> {
            setupViewport(viewport);
          });
    }

    private void setupViewport(RawViewport viewport) {
      remainingViewportCount++;
      AnimatedLayer animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport.getExtent());
      AnimatedLayerPlayer.playDecay(animation, viewport);
      viewport.addListener(
          new Button2Listener() {
            public void onButtonPressed(Point p) {
              animation.stop();
              viewport.getRawLayer().setAllPixels(Color.BLACK);
              viewport.removeListener(this);
              appendViewport(viewport);
            }
          });
    }

    // Add this viewport to the right of the previously chosen viewports.
    // If this is last one, return final viewport.
    private void appendViewport(RawViewport viewport) {
      if (teardownTemporaryViewport != null) {
        teardownTemporaryViewport.run();
      }
      viewportBuilder.add(viewport, Point.create(nextX, 0));
      nextX += viewport.getExtent().width();
      remainingViewportCount--;

      if (remainingViewportCount > 0) {
        RawViewport temporaryViewport = viewportBuilder.build();
        AnimatedLayer animation =
            SELECTED_VIEWPORT_ANIMATION.newAnimation(temporaryViewport.getExtent());
        AnimatedLayerPlayer.playDecay(animation, temporaryViewport);
        teardownTemporaryViewport =
            (() -> {
              animation.stop();
              temporaryViewport.getRawLayer().setAllPixels(Color.BLACK);
            });
      } else {
        RawViewport chosenViewport = viewportBuilder.build();
        Spark spark =
            new Spark(chosenViewport.getExtent(), chosenViewport.getExtent().middle(), Color.BLUE);
        AnimatedLayerPlayer.playDecay(spark, chosenViewport);
        futureViewport.set(chosenViewport);
      }
    }

    public ListenableFuture<RawViewport> getFutureViewport() {
      return futureViewport;
    }
  }
}
