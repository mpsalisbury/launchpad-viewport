package com.salisburyclan.lpviewport.layout;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.animation.AnimationProvider;
import com.salisburyclan.lpviewport.animation.BorderSweep;
import com.salisburyclan.lpviewport.animation.EdgeSweep;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.viewport.Edge;
import com.salisburyclan.lpviewport.viewport.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkedPairsLayoutProvider implements LayoutProvider {

  private static final String TYPE = "linked";
  private static final String DESCRIPTION =
      TYPE + " : combines the viewports by user selecting attachment points";
  private static final AnimationProvider AWAITING_SELECTION_ANIMATION =
      BorderSweep.newProvider(Color.RED);
  private static final AnimationProvider SELECTED_VIEWPORT_ANIMATION =
      Sweep.newProvider(Color.BLUE, false);

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
    List<Viewport> viewports =
        devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList());

    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("Empty LayoutSpec: " + layoutSpec);
    } else if (viewports.size() == 1) {
      return Futures.immediateFuture(viewports.get(0));
    } else {
      return makeLinkedViewport(
          devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList()));
    }
  }

  private ListenableFuture<Viewport> makeLinkedViewport(List<Viewport> viewports) {
    return new ViewportBuilder(viewports).getFutureViewport();
  }

  private static class ViewportBuilder {
    private SettableFuture<Viewport> futureViewport;
    private List<Viewport> unselectedViewports;
    private List<Viewport> selectedViewports;
    private List<Runnable> viewportCleanups;
    private LinkAssembler linkAssembler;

    // Represents a link between two viewports.
    // (fromX,fromY) must be along edge of fromViewport.
    // (toX,toY) must be along opposite edge of toViewport.
    private static class Link {
      public Viewport fromViewport;
      public Edge fromEdge;
      // fromLink, in fromViewport space
      public int fromX;
      public int fromY;
      public Viewport toViewport;
      // toLink, in toViewport space
      public int toX;
      public int toY;

      public Link(Viewport fromViewport, Edge fromEdge, int fromX, int fromY) {
        this.fromViewport = fromViewport;
        this.fromEdge = fromEdge;
        this.fromX = fromX;
        this.fromY = fromY;
      }

      public String toString() {
        return String.format(
            "Link(%s, %s, (%d, %d), %s, (%d, %d))",
            fromViewport, fromEdge, fromX, fromY, toViewport, toX, toY);
      }

      // Returns the offset between the origin of fromViewport and the origin of toViewport
      // in absolute space.
      public Point getOriginOffset() {
        ViewExtent toExtent = toViewport.getExtent();
        int normalToX = toX - toExtent.getXLow();
        int normalToY = toY - toExtent.getYLow();
        Point linkTarget = fromLinkTarget();
        return new Point(linkTarget.x - normalToX, linkTarget.y - normalToY);
      }

      // Returns the point of the button in the toViewport that should connect to
      // the fromLinked button, in normalized fromViewport space.
      private Point fromLinkTarget() {
        ViewExtent fromExtent = fromViewport.getExtent();
        int normalFromX = fromX - fromExtent.getXLow();
        int normalFromY = fromY - fromExtent.getYLow();
        switch (fromEdge) {
          case LEFT:
            return new Point(normalFromX - 1, normalFromY);
          case RIGHT:
            return new Point(normalFromX + 1, normalFromY);
          case TOP:
            return new Point(normalFromX, normalFromY + 1);
          case BOTTOM:
            return new Point(normalFromX, normalFromY - 1);
          default:
            throw new IllegalStateException("Invalid edge type");
        }
      }
    }

    public ViewportBuilder(List<Viewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.unselectedViewports = new ArrayList<>();
      this.selectedViewports = new ArrayList<>();
      this.linkAssembler = new LinkAssembler();
      this.viewportCleanups = new ArrayList<>();
      // First choose edge of any Viewport
      viewports.forEach(
          viewport -> {
            unselectedViewports.add(viewport);
            waitForLinkStart(viewport);
          });
    }

    // Accept a click on any edge and then wait for linked click.
    private void waitForLinkStart(Viewport viewport) {
      Animation animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport);
      animation.play();
      ViewportListener listener =
          new ViewportListener() {
            public void onButtonPressed(int x, int y) {
              if (Edge.getEdge(viewport.getExtent(), x, y) != Edge.INVALID) {
                deactivateViewports();
                selectLinkStart(viewport, x, y);
              }
            }

            public void onButtonReleased(int x, int y) {}
          };
      viewport.addListener(listener);
      viewportCleanups.add(
          () -> {
            animation.stop();
            viewport.setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private void deactivateViewports() {
      viewportCleanups.forEach(Runnable::run);
      viewportCleanups.clear();
    }

    private void selectLinkStart(Viewport startViewport, int startX, int startY) {
      // if this is the first selection, startViewport needs to be moved
      // from unselected to selected.
      if (unselectedViewports.remove(startViewport)) {
        selectedViewports.add(startViewport);
      }
      Edge selectedEdge = Edge.getEdge(startViewport.getExtent(), startX, startY);
      Edge oppositeEdge = selectedEdge.getOpposite();
      Link partialLink = new Link(startViewport, selectedEdge, startX, startY);
      unselectedViewports.forEach(
          viewport -> {
            waitForLinkEnd(viewport, oppositeEdge, partialLink);
          });
    }

    // Accept a click on only one edge and register the link.
    private void waitForLinkEnd(Viewport viewport, Edge requiredEdge, Link partialLink) {
      Animation animation = new EdgeSweep(viewport, requiredEdge, Color.RED);
      animation.play();
      ViewportListener listener =
          new ViewportListener() {
            public void onButtonPressed(int x, int y) {
              if (requiredEdge.isEdge(viewport.getExtent(), x, y)) {
                deactivateViewports();
                selectLinkEnd(partialLink, viewport, x, y);
              }
            }

            public void onButtonReleased(int x, int y) {}
          };
      viewport.addListener(listener);
      viewportCleanups.add(
          () -> {
            animation.stop();
            viewport.setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private void selectLinkEnd(Link partialLink, Viewport endViewport, int endX, int endY) {
      unselectedViewports.remove(endViewport);
      selectedViewports.add(endViewport);

      partialLink.toViewport = endViewport;
      partialLink.toX = endX;
      partialLink.toY = endY;
      linkAssembler.addLink(partialLink);

      if (unselectedViewports.isEmpty()) {
        finalizeViewport();
      } else {
        selectedViewports.forEach(
            viewport -> {
              waitForLinkStart(viewport);
            });
      }
    }

    private void finalizeViewport() {
      Viewport finalViewport = linkAssembler.build();
      SELECTED_VIEWPORT_ANIMATION.newAnimation(finalViewport).play();
      futureViewport.set(finalViewport);
    }

    public ListenableFuture<Viewport> getFutureViewport() {
      return futureViewport;
    }

    // Constructs a Viewport given the links between subviewports.
    private static class LinkAssembler {
      private Map<Viewport, Point> viewportOrigins;
      private AggregateViewport.Builder viewportBuilder;

      public LinkAssembler() {
        this.viewportOrigins = new HashMap<>();
        this.viewportBuilder = new AggregateViewport.Builder();
      }

      public void addLink(Link link) {
        if (viewportOrigins.isEmpty()) {
          addViewport(link.fromViewport, new Point(0, 0));
        }
        Point toViewportOrigin = computeToViewportOrigin(link);
        addViewport(link.toViewport, toViewportOrigin);
      }

      private void addViewport(Viewport viewport, Point origin) {
        viewportBuilder.add(viewport, origin.x, origin.y);
        viewportOrigins.put(viewport, origin);
      }

      private Point computeToViewportOrigin(Link link) {
        Point fromOrigin = viewportOrigins.get(link.fromViewport);
        Point originOffset = link.getOriginOffset();
        return new Point(fromOrigin.x + originOffset.x, fromOrigin.y + originOffset.y);
      }

      public Viewport build() {
        return viewportBuilder.build();
      }
    }
  }
}
