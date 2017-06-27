package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.DecayingAnimation;
import com.salisburyclan.lpviewport.animation.DiagRotate;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Range2;

public class DiagRotateApp extends JavafxLaunchpadApplication {

  private static final Color BOX_COLOR = Color.BLUE;
  private static final Color BAR_COLOR = Color.ORANGE;
  private WriteLayer outputLayer;
  private Range2 barExtent;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.outputLayer = viewport.addLayer();
    renderBox();

    barExtent = viewport.getExtent().inset(1, 1, 1, 1);
    DiagRotate diagRotate = new DiagRotate(barExtent, BAR_COLOR);
    viewport.addLayer(new DecayingAnimation(diagRotate));
    diagRotate.play();
  }

  private void renderBox() {
    Range2 extent = outputLayer.getExtent();
    extent
        .xRange()
        .forEach(
            x -> {
              outputLayer.setPixel(x, extent.yRange().low(), BOX_COLOR);
              outputLayer.setPixel(x, extent.yRange().high(), BOX_COLOR);
            });
    extent
        .yRange()
        .forEach(
            y -> {
              outputLayer.setPixel(extent.xRange().low(), y, BOX_COLOR);
              outputLayer.setPixel(extent.xRange().high(), y, BOX_COLOR);
            });
  }
}
