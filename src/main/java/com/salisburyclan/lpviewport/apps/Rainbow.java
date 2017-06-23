package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.WriteLayer;

public class Rainbow extends JavafxLaunchpadApplication {

  private WriteLayer outputLayer;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.outputLayer = viewport.addLayer();
    setRainbow();
  }

  private void setRainbow() {
    outputLayer
        .getExtent()
        .xRange()
        .forEach(
            x -> {
              setBar(x, getColor(x));
            });
  }

  // Returns a color for the given index.
  // Cycle forward and backward through color list.
  private DColor getColor(int index) {
    final DColor colors[] = {
      DColor.RED,
      DColor.ORANGE,
      DColor.YELLOW,
      DColor.YELLOW_GREEN,
      DColor.GREEN,
      DColor.BLUE,
      DColor.MAGENTA,
      DColor.PURPLE,
    };

    int cycleLength = (colors.length - 1) * 2;
    int indexWithinCycle = index % cycleLength;
    if (indexWithinCycle >= colors.length) {
      return colors[cycleLength - indexWithinCycle];
    } else {
      return colors[indexWithinCycle];
    }
  }

  private void setBar(int x, DColor color) {
    outputLayer
        .getExtent()
        .yRange()
        .forEach(
            y -> {
              outputLayer.setPixel(x, y, color);
            });
  }
}
