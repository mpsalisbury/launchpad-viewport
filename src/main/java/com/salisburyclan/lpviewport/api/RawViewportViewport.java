package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.Layer;
import com.salisburyclan.lpviewport.layer.LayerBuffer;
import com.salisburyclan.lpviewport.layer.LayerSandwich;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.PixelListener;

// Viewport is a rectangular set of buttons/lights.
public class RawViewportViewport implements Viewport {
  private RawViewport rawViewport;
  private LayerSandwich layers;

  public RawViewportViewport(RawViewport rawViewport) {
    this.rawViewport = rawViewport;
    this.layers = new LayerSandwich(rawViewport.getExtent());
    layers.addPixelListener(
        new PixelListener() {
          @Override
          public void onNextFrame() {}

          @Override
          public void onSetPixel(int x, int y) {
            writeRawPixel(x, y);
          }
        });
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
