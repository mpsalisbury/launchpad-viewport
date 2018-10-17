package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
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
          public void onPixelChanged(Point p) {
            writeRawPixel(p.x(), p.y());
          }

          @Override
          public void onPixelsChanged(Range2 range) {
            writeRawPixels(range);
          }
        });
  }

  @Override
  public Range2 getExtent() {
    return rawViewport.getExtent();
  }

  // Adds and returns an output layer.
  @Override
  public LayerBuffer addLayer() {
    LayerBuffer buffer = new LayerBuffer(getExtent());
    addLayer(buffer);
    return buffer;
  }

  @Override
  public void addLayer(ReadLayer layer) {
    layers.addLayer(layer);
  }

  @Override
  public void removeLayer(ReadLayer layer) {
    layers.removeLayer(layer);
  }

  @Override
  public void removeAllLayers() {
    layers.removeAllLayers();
  }

  // Write pixel from our layers into the raw viewport.
  private void writeRawPixel(int x, int y) {
    Pixel pixel = Pixel.BLACK.combine(layers.getPixel(x, y));
    rawViewport.getRawLayer().setPixel(x, y, pixel.color());
  }

  // Write pixels from our layers into the raw viewport.
  private void writeRawPixels(Range2 range) {
    range.forEach(this::writeRawPixel);
  }

  // Adds a listener for this viewport.
  @Override
  public void addListener(Button2Listener listener) {
    rawViewport.addListener(listener);
  }

  @Override
  public void removeListener(Button2Listener listener) {
    rawViewport.removeListener(listener);
  }

  @Override
  public void removeAllListeners() {
    rawViewport.removeAllListeners();
  }
}
