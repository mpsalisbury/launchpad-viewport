package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.viewport.StripSubViewButton;
import com.salisburyclan.lpviewport.viewport.SubViewButton;
import com.salisburyclan.lpviewport.viewport.SubViewStrip;
import com.salisburyclan.lpviewport.viewport.SubViewport;

// Utility to generate SubViews of a given Viewport.
public class SubView {
  private SubView() {}

  // Returns a new viewport relative to this one.
  public static Viewport getSubViewport(Viewport viewport, Range2 extent) {
    return new SubViewport(viewport, extent);
  }

  // Returns a new ViewStrip relative to this viewport.
  // extent must be one button wide or one button high.
  public static Viewport1 getSubViewStrip(Viewport viewport, Range2 extent) {
    return new SubViewStrip(viewport, extent);
  }

  // Returns a new ViewButton relative to this viewport.
  public static Viewport0 getSubViewport0(Viewport viewport, Point p) {
    return new SubViewButton(viewport, p);
  }

  // Returns a new ViewButton relative to this viewstrip.
  public static Viewport0 getSubViewport0(Viewport1 viewstrip, int x) {
    return new StripSubViewButton(viewstrip, x);
  }
}
