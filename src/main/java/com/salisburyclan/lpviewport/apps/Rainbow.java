package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;

public class Rainbow extends JavafxLaunchpadApplication {

  private LightLayer outputLayer;

  @Override
  public void run() {
    getRawViewport(this::setupViewport);
  }

  private void setupViewport(RawViewport viewport) {
    this.outputLayer = viewport.getLightLayer();
    setRainbow();
  }

  private void setRainbow() {
    outputLayer
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
    outputLayer
        .getExtent()
        .yRange()
        .stream()
        .forEach(
            y -> {
              outputLayer.setLight(x, y, color);
            });
  }
}
