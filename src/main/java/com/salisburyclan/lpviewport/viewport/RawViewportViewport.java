package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.LayerSandwich;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;

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

  public void addLayer(ReadLayer layer) {
    layers.addLayer(layer);
  }

  public void removeLayer(ReadLayer layer) {
    layers.removeLayer(layer);
  }

  private void writeRawPixel(int x, int y) {
    Pixel pixel = Pixel.BLACK.combine(layers.getPixel(x, y));
    rawViewport.getRawLayer().setPixel(x, y, pixel.color());
  }

  // Adds a listener for this viewport.
  public void addListener(Button2Listener listener) {
    rawViewport.addListener(listener);
  }

  public void removeListener(Button2Listener listener) {
    rawViewport.removeListener(listener);
  }
}
