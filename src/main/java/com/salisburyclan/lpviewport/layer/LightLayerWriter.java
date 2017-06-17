package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.geom.Range2;

// Writes into a LightLayer;
public class LightLayerWriter implements WriteLayer {
  private LightLayer lightLayer;
  private Range2 extent;

  public LightLayerWriter(LightLayer lightLayer) {
    this.lightLayer = lightLayer;
    this.extent = lightLayer.getExtent();
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void setPixel(int x, int y, DColor color) {
    if (extent.isPointWithin(x, y)) {
      lightLayer.setLight(x, y, color.color());
    }
  }

  @Override
  public void setPixel(int x, int y, Pixel pixel) {
    setPixel(x, y, Pixel.BLACK.combine(pixel).color());
  }

  @Override
  public void close() {
    // Nobody listening to us. Nothing to close.
  }
}
