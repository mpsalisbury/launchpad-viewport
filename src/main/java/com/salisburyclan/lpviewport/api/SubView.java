package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.viewport.StripSubViewport0;
import com.salisburyclan.lpviewport.viewport.SubViewport;
import com.salisburyclan.lpviewport.viewport.SubViewport0;
import com.salisburyclan.lpviewport.viewport.SubViewport1;

// Utility to generate SubViews of a given Viewport.
public class SubView {
  private SubView() {}

  // Returns a new viewport relative to this one.
  public static Viewport getSubViewport(Viewport viewport, Range2 extent) {
    return new SubViewport(viewport, extent);
  }

  // Returns a new one-dimensional viewport relative to this viewport.
  // extent must be one button wide or one button high.
  public static Viewport1 getSubViewport1(Viewport viewport, Range2 extent) {
    return new SubViewport1(viewport, extent);
  }

  // Returns a new one-button viewport relative to this viewport.
  public static Viewport0 getSubViewport0(Viewport viewport, Point p) {
    return new SubViewport0(viewport, p);
  }

  // Returns a new one-button viewport relative to this viewstrip.
  public static Viewport0 getSubViewport0(Viewport1 viewstrip, int x) {
    return new StripSubViewport0(viewstrip, x);
  }
}
