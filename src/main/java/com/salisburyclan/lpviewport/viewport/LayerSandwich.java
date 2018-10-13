package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.CloseListener;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.api.ReadLayer;
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

  // Adds the given layer as the top layer of this sandwich.
  // Given layer must not extend beyond the extent of the sandwich.
  public void addLayer(ReadLayer layer) {
    if (!extent.isRangeWithin(layer.getExtent())) {
      throw new IllegalArgumentException("LayerSandwich::addLayer has incorrect extent");
    }
    layer.addPixelListener(pixelListeners);
    layer.addCloseListener(() -> removeLayer(layer));
    layers.add(layer);
    pixelListeners.onPixelsChanged(layer.getExtent());
  }

  public void removeLayer(ReadLayer layer) {
    layer.removePixelListener(pixelListeners);
    layers.remove(layer);
    pixelListeners.onPixelsChanged(layer.getExtent());
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
  public void removeAllPixelListeners() {
    pixelListeners.clear();
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    closeListeners.add(listener);
  }
}
