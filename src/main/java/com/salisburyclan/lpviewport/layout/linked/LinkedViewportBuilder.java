package com.salisburyclan.lpviewport.layout.linked;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.animation.AnimationProvider;
import com.salisburyclan.lpviewport.animation.BorderSweep;
import com.salisburyclan.lpviewport.animation.EdgeSweep;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import java.util.ArrayList;
import java.util.List;

public class LinkedViewportBuilder {
  private static final AnimationProvider AWAITING_SELECTION_ANIMATION =
      BorderSweep.newProvider(Color.RED);
  private static final AnimationProvider SELECTED_VIEWPORT_ANIMATION =
      Sweep.newProvider(Color.BLUE, false);

  private SettableFuture<RawViewport> futureViewport;
  private List<RawViewport> unselectedViewports;
  private List<RawViewport> selectedViewports;
  private List<Runnable> viewportCleanups;
  private LinkAssembler linkAssembler;

  public LinkedViewportBuilder(List<RawViewport> viewports) {
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
  private void waitForLinkStart(RawViewport viewport) {
    Animation animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport);
    animation.play();
    Button2Listener listener =
        new Button2Listener() {
          public void onButtonPressed(Point p) {
            if (Edge.getEdge(viewport.getExtent(), p) != Edge.INVALID) {
              deactivateViewports();
              selectLinkStart(viewport, p);
            }
          }

          public void onButtonReleased(Point p) {}
        };
    viewport.addListener(listener);
    viewportCleanups.add(
        () -> {
          animation.stop();
          viewport.getLightLayer().setAllLights(Color.BLACK);
          viewport.removeListener(listener);
        });
  }

  private void deactivateViewports() {
    viewportCleanups.forEach(Runnable::run);
    viewportCleanups.clear();
  }

  private void selectLinkStart(RawViewport startViewport, Point start) {
    // if this is the first selection, startViewport needs to be moved
    // from unselected to selected.
    if (unselectedViewports.remove(startViewport)) {
      selectedViewports.add(startViewport);
    }
    Edge selectedEdge = Edge.getEdge(startViewport.getExtent(), start);
    Edge oppositeEdge = selectedEdge.getOpposite();
    Link partialLink = new Link(startViewport, selectedEdge, start);
    unselectedViewports.forEach(
        viewport -> {
          waitForLinkEnd(viewport, oppositeEdge, partialLink);
        });
  }

  // Accept a click on only one edge and register the link.
  private void waitForLinkEnd(RawViewport viewport, Edge requiredEdge, Link partialLink) {
    Animation animation = new EdgeSweep(viewport, requiredEdge, Color.RED);
    animation.play();
    Button2Listener listener =
        new Button2Listener() {
          public void onButtonPressed(Point p) {
            if (requiredEdge.isEdge(viewport.getExtent(), p)) {
              deactivateViewports();
              selectLinkEnd(partialLink, viewport, p);
            }
          }

          public void onButtonReleased(Point p) {}
        };
    viewport.addListener(listener);
    viewportCleanups.add(
        () -> {
          animation.stop();
          viewport.getLightLayer().setAllLights(Color.BLACK);
          viewport.removeListener(listener);
        });
  }

  private void selectLinkEnd(Link partialLink, RawViewport endViewport, Point endPoint) {
    unselectedViewports.remove(endViewport);
    selectedViewports.add(endViewport);

    partialLink.toViewport = endViewport;
    partialLink.toPoint = endPoint;
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
    RawViewport finalViewport = linkAssembler.build();
    //      SELECTED_VIEWPORT_ANIMATION.newAnimation(finalViewport).play();
    futureViewport.set(finalViewport);
  }

  public ListenableFuture<RawViewport> getFutureViewport() {
    return futureViewport;
  }
}
