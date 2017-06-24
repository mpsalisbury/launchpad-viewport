package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.DiagRotate;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.WriteLayer;

public class DiagRotateApp extends JavafxLaunchpadApplication {

  private static final DColor BOX_COLOR = DColor.BLUE;
  private static final DColor BAR_COLOR = DColor.ORANGE;
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
    DiagRotate diagRotate = new DiagRotate(viewport.getExtent(), barExtent, BAR_COLOR);
    viewport.addLayer(diagRotate);
    //viewport.addLayer(new DecayingAnimation(diagRotate));
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
