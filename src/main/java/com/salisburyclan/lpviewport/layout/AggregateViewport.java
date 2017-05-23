package com.salisburyclan.lpviewport.layout;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewButtonListener;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;

import java.util.ArrayList;
import java.util.List;

// A viewport that represents multiple viewports stitched together
public class AggregateViewport implements Viewport {

  // Represents a viewport that composes part of this AggregateViewport.
  private static class Viewpart {
    public Viewport viewport;
    // The extent of this viewport within the AggregateViewport.
    public ViewExtent extent;
    // viewport.exent + offset = this.extent
    public int xOffset;
    public int yOffset;

    public Viewpart(Viewport viewport, ViewExtent extent, int xOffset, int yOffset) {
      this.viewport = viewport;
      this.extent = extent;
      this.xOffset = xOffset;
      this.yOffset = yOffset;
    }
  }
  private List<Viewpart> viewparts;
  private ViewExtent extent;

  private AggregateViewport(List<Viewpart> viewparts, ViewExtent extent) {
    this.viewparts = viewparts;
    this.extent = extent;
  }

  // Builds an AggregateViewport
  public static class Builder {
    private List<Viewpart> viewparts;

    public Builder() {
      this.viewparts = new ArrayList<>();
    }

    // Adds the given viewport with the low corner placed at
    // (xLow, yLow) in this aggregate viewport.
    public void add(Viewport viewport, int targetXLow, int targetYLow) {
      ViewExtent oldExtent = viewport.getExtent();
      int xOffset = targetXLow - oldExtent.getXLow();
      int yOffset = targetYLow - oldExtent.getYLow();
      ViewExtent newExtent = viewport.getExtent().shift(xOffset, yOffset);
      viewparts.add(new Viewpart(viewport, newExtent, xOffset, yOffset));
    }

    public AggregateViewport build() {
      if (viewparts.isEmpty()) {
        throw new IllegalArgumentException("Must add at least one Viewport to AggregateViewport");
      }
      return new AggregateViewport(viewparts, computeExtent());
    }

    private ViewExtent computeExtent() {
      return viewparts.stream()
        .map(viewpart -> viewpart.extent)
        .reduce(viewparts.get(0).extent, (a, b) -> a.includeBoth(b));
    }
  }

  @Override
  public ViewExtent getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    viewparts.forEach(viewpart -> {
      if (viewpart.extent.isPointWithin(x, y)) {
        viewpart.viewport.setLight(x - viewpart.xOffset, y - viewpart.yOffset, color);
      }
    });
  }

  @Override
  public void setAllLights(Color color) {
    viewparts.forEach(viewpart -> {
      viewpart.viewport.setAllLights(color);
    });
  }

  @Override
  public void addListener(ViewportListener listener) {
    viewparts.forEach(viewpart -> {
      viewpart.viewport.addListener(new ViewportListener() {
        public void onButtonPressed(int x, int y) {
          listener.onButtonPressed(x + viewpart.xOffset, y + viewpart.yOffset);
        }
        public void onButtonReleased(int x, int y) {
          listener.onButtonReleased(x + viewpart.xOffset, y + viewpart.yOffset);
        }
      });
    });
  }

  @Override
  public void removeListener(ViewportListener listener) {
    // TODO implement
    throw new UnsupportedOperationException("AggregateViewport::removeListener");
  }
}

