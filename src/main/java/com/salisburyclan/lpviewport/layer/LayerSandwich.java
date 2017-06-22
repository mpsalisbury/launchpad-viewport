package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Range2;
import java.util.ArrayList;
import java.util.List;

// A layer consisting of multiple layers.
public class LayerSandwich implements Layer {
  private Range2 extent;
  // Layers in this sandwich, ordered bottom-to-top.
  private List<Layer> layers;
  private PixelListenerMultiplexer pixelListeners;
  private CloseListenerMultiplexer closeListeners;

  public LayerSandwich(Range2 extent) {
    this.extent = extent;
    this.layers = new ArrayList<>();
    this.pixelListeners = new PixelListenerMultiplexer();
    this.closeListeners = new CloseListenerMultiplexer();
  }

  public void addLayer(Layer layer) {
    if (!layer.getExtent().equals(extent)) {
      throw new IllegalArgumentException("LayerSandwich::addLayer has incorrect extent");
    }
    layer.addPixelListener(pixelListeners);
    layer.addCloseListener(() -> removeLayer(layer));
    layers.add(layer);
  }

  public void removeLayer(Layer layer) {
    layers.remove(layer);
  }

  public void close() {
    closeListeners.onClose();
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    Pixel pixel = Pixel.EMPTY;
    for (Layer layer : layers) {
      pixel = pixel.combine(layer.getPixel(x, y));
    }
    return pixel;
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.addListener(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.removeListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    closeListeners.addListener(listener);
  }

  // TODO implement
  //public Layer flatten() { }
}
