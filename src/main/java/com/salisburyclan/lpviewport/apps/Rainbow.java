package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;

public class Rainbow extends JavafxLaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.viewport = viewport;
    setRainbow();
  }

  private void setRainbow() {
    viewport
        .getExtent()
        .xRange()
        .stream()
        .forEach(
            x -> {
              setBar(x, getColor(x));
            });
  }

  // Returns a color for the given index.
  // Cycle forward and backward through color list.
  private Color getColor(int index) {
    final Color colors[] = {
      Color.RED,
      Color.ORANGE,
      Color.YELLOW,
      Color.YELLOW_GREEN,
      Color.GREEN,
      Color.BLUE,
      Color.MAGENTA,
      Color.PURPLE,
    };

    int cycleLength = (colors.length - 1) * 2;
    int indexWithinCycle = index % cycleLength;
    if (indexWithinCycle >= colors.length) {
      return colors[cycleLength - indexWithinCycle];
    } else {
      return colors[indexWithinCycle];
    }
  }

  private void setBar(int x, Color color) {
    viewport
        .getExtent()
        .yRange()
        .stream()
        .forEach(
            y -> {
              viewport.setLight(x, y, color);
            });
  }
}
