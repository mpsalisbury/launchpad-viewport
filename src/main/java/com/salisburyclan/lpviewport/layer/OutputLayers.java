package com.salisburyclan.lpviewport.layer;

// Copies the layers into the viewport.
public class OutputLayers {
  /*
  // Underlying layer to write to.
  private LightLayer outputLayer;
  // Buffers that clients draw in.
  private LayerSandwich layers;

  public OutputLayers(LightLayer outputLayer) {
    this.outputLayer = outputLayer;
    Range2 extent = outputLayer.getExtent();
    this.layers = new LayerSandwich(extent);
  }

  // Returns the layer from a new single-layer instance.
  public static LayerBuffer createSingleLayer(LightLayer outputLayer) {
    return new OutputLayers(outputLayer).newLayer();
  }

  public Range2 getExtent() {
    return outputLayer.getExtent();
  }

  public LayerBuffer newLayer() {
    LayerBuffer layer = new LayerBuffer(outputLayer.getExtent());
    layers.addLayer(layer);
    layer.addListener(this::writePixel);
    layer.setCloser(() -> removeLayer(layer));
    return layer;
  }

  public void addLayer(Layer layer) {
    if (!layer.getExtent().equals(layers.getExtent())) {
      throw new IllegalArgumentException("Layer has invalid extent");
    }
    layers.addLayer(layer);
    layer.addListener(this::writePixel);
    layer.setCloser(() -> removeLayer(layer));
  }

  public void removeLayer(Layer layer) {
    layers.removeLayer(layer);
  }

  // Write pixel into outputLayer.
  private void writePixel(int x, int y) {
    Pixel pixel = Pixel.BLACK.combine(layers.getPixel(x, y));
    outputLayer.setLight(x, y, pixel.color().color());
  }
  */
}
