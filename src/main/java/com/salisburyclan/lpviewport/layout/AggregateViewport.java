package com.salisburyclan.lpviewport.layout;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.viewport.RawLayer;
import com.salisburyclan.lpviewport.viewport.RawViewport;
import java.util.ArrayList;
import java.util.List;

// A viewport that represents multiple viewports stitched together
public class AggregateViewport implements RawViewport {
  private List<Viewpart> viewparts;
  private Range2 extent;
  private RawLayer outputLayer;

  private AggregateViewport(List<Viewpart> viewparts, Range2 extent) {
    this.viewparts = viewparts;
    this.extent = extent;
    this.outputLayer = new AggregateRawLayer();
  }

  // Builds an AggregateViewport
  public static class Builder {
    private List<Viewpart> viewparts;

    public Builder() {
      this.viewparts = new ArrayList<>();
    }

    // Adds the given viewport with the low corner placed at
    // (originX, originY) in this aggregate viewport.
    public void add(RawViewport viewport, Point origin) {
      Range2 oldExtent = viewport.getExtent();
      Vector offset = origin.subtract(oldExtent.low());
      Range2 newExtent = viewport.getExtent().shift(offset);
      viewparts.add(new Viewpart(viewport, newExtent, offset));
    }

    public AggregateViewport build() {
      if (viewparts.isEmpty()) {
        throw new IllegalArgumentException("Must add at least one Viewport to AggregateViewport");
      }
      // TODO defensive copy of viewparts in case of multiple calls to build()
      return new AggregateViewport(viewparts, computeExtent());
    }

    private Range2 computeExtent() {
      return viewparts
          .stream()
          .map(viewpart -> viewpart.extent)
          .reduce(viewparts.get(0).extent, (a, b) -> a.includeBoth(b));
    }
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public RawLayer getRawLayer() {
    return outputLayer;
  }

  @Override
  public void addListener(Button2Listener listener) {
    viewparts.forEach(
        viewpart -> {
          viewpart.viewport.addListener(
              new Button2Listener() {
                public void onButtonPressed(Point p) {
                  listener.onButtonPressed(p.add(viewpart.offset));
                }

                public void onButtonReleased(Point p) {
                  listener.onButtonReleased(p.add(viewpart.offset));
                }
              });
        });
  }

  @Override
  public void removeListener(Button2Listener listener) {
    // TODO implement
    throw new UnsupportedOperationException("AggregateViewport::removeListener");
  }

  @Override
  public void removeAllListeners() {
    // TODO implement
    throw new UnsupportedOperationException("AggregateViewport::removeAllListeners");
  }

  // Represents a viewport that composes part of this AggregateViewport.
  private static class Viewpart {
    public RawViewport viewport;
    // The extent of this viewport within the AggregateViewport.
    public Range2 extent;
    // viewport.exent + offset = this.extent
    public Vector offset;

    public Viewpart(RawViewport viewport, Range2 extent, Vector offset) {
      this.viewport = viewport;
      this.extent = extent;
      this.offset = offset;
    }
  }

  private class AggregateRawLayer implements RawLayer {
    @Override
    public Range2 getExtent() {
      return extent;
    }

    @Override
    public void setPixel(int x, int y, Color color) {
      Point p = Point.create(x, y);
      viewparts.forEach(
          viewpart -> {
            if (viewpart.extent.isPointWithin(p)) {
              viewpart.viewport.getRawLayer().setPixel(p.subtract(viewpart.offset), color);
            }
          });
    }

    @Override
    public void setAllPixels(Color color) {
      viewparts.forEach(
          viewpart -> {
            viewpart.viewport.getRawLayer().setAllPixels(color);
          });
    }
  }
}
