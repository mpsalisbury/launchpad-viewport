package com.salisburyclan.lpviewport.api;

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
  public static ViewStrip getSubViewStrip(Viewport viewport, Range2 extent) {
    return new SubViewStrip(viewport, extent);
  }

  // Returns a new ViewButton relative to this viewport.
  public static ViewButton getSubViewButton(Viewport viewport, int x, int y) {
    return new SubViewButton(viewport, x, y);
  }

  // Returns a new ViewButton relative to this viewstrip.
  public static ViewButton getSubViewButton(ViewStrip viewstrip, int x) {
    return new StripSubViewButton(viewstrip, x);
  }
}
