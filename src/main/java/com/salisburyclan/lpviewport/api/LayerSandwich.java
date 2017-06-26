package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CloseListenerMultiplexer;
import com.salisburyclan.lpviewport.util.PixelListenerMultiplexer;
import java.util.ArrayList;
import java.util.List;

// A layer consisting of multiple layers.
public class LayerSandwich implements ReadLayer {
  private Range2 extent;
  // Layers in this sandwich, ordered bottom-to-top.
  private List<ReadLayer> layers;
  private PixelListenerMultiplexer pixelListeners;
  private CloseListenerMultiplexer closeListeners;

  public LayerSandwich(Range2 extent) {
    this.extent = extent;
    this.layers = new ArrayList<>();
    this.pixelListeners = new PixelListenerMultiplexer();
    this.closeListeners = new CloseListenerMultiplexer();
  }

  public void addLayer(ReadLayer layer) {
    if (!layer.getExtent().equals(extent)) {
      throw new IllegalArgumentException("LayerSandwich::addLayer has incorrect extent");
    }
    layer.addPixelListener(pixelListeners);
    layer.addCloseListener(() -> removeLayer(layer));
    layers.add(layer);
  }

  public void removeLayer(ReadLayer layer) {
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
    for (ReadLayer layer : layers) {
      pixel = pixel.combine(layer.getPixel(x, y));
    }
    return pixel;
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.add(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.remove(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    closeListeners.add(listener);
  }

  // TODO implement
  //public Layer flatten() { }
}
