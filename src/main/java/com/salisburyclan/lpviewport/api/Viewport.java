package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.LayerBuffer;
import com.salisburyclan.lpviewport.layer.LayerSandwich;
import com.salisburyclan.lpviewport.layer.Pixel;

// Viewport is a rectangular set of buttons/lights.
public class Viewport {
  private RawViewport rawViewport;
  private LayerSandwich layers;

  public Viewport(RawViewport rawViewport) {
    this.rawViewport = rawViewport;
    this.layers = new LayerSandwich(rawViewport.getExtent());
    layers.addPixelListener(this::writeRawPixel);
  }

  public Range2 getExtent() {
    return rawViewport.getExtent();
  }

  // Adds and returns an output layer.
  public LayerBuffer addLayer() {
    LayerBuffer buffer = new LayerBuffer(getExtent());
    addLayer(buffer);
    return buffer;
  }

  public void addLayer(Layer layer) {
    layers.addLayer(layer);
  }

  private void writeRawPixel(int x, int y) {
    Pixel pixel = Pixel.BLACK.combine(layers.getPixel(x, y));
    rawViewport.getLightLayer().setLight(x, y, pixel.color().color());
  }

  // Adds a listener for this viewport.
  public void addListener(Button2Listener listener) {
    rawViewport.addListener(listener);
  }

  public void removeListener(Button2Listener listener) {
    rawViewport.removeListener(listener);
  }
}
