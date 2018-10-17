package com.salisburyclan.lpviewport.testing;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.Multiplexer;
import com.salisburyclan.lpviewport.viewport.LayerSandwich;

// Sample Viewport for testing subviews.
public class TestViewport implements Viewport {
  private Range2 extent;
  private LayerSandwich layers;
  private Button2ListenerMultiplexer listeners;

  public TestViewport(Range2 extent) {
    this.extent = extent;
    this.layers = new LayerSandwich(extent);
    this.listeners = new Button2ListenerMultiplexer();
  }

  public Pixel getPixel(Point p) {
    return layers.getPixel(p);
  }

  // Simulate the pushing of a button at Point p.
  public void pushButton(Point p) {
    listeners.onButtonPressed(p);
  }

  // Simulate the pushing of a button at Point p.
  public void releaseButton(Point p) {
    listeners.onButtonReleased(p);
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

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

  @Override
  public void addListener(Button2Listener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(Button2Listener listener) {
    listeners.remove(listener);
  }

  @Override
  public void removeAllListeners() {
    listeners.clear();
  }

  private static class Button2ListenerMultiplexer extends Multiplexer<Button2Listener>
      implements Button2Listener {
    @Override
    public void onButtonPressed(Point p) {
      getItemsCopy().forEach(listener -> listener.onButtonPressed(p));
    }

    @Override
    public void onButtonReleased(Point p) {
      getItemsCopy().forEach(listener -> listener.onButtonReleased(p));
    }
  }
}
