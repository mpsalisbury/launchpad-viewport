package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.util.PixelListenerMultiplexer;
import java.util.HashMap;
import java.util.Map;

public class LayerTranslator implements ReadLayer {
  private ReadLayer innerLayer;

  // Offset of innerLayer from this Layer
  // (e.g. offset(2,2) means that getPixel(2,2) will return innerLayer(0,0))
  private Vector offset;

  // Keep track of derived listeners so we can remove them
  // from innerLayer upon request. original -> derived.
  private Map<PixelListener, PixelListener> listenerMap;

  // For fanning out pixelListener notifications.
  private PixelListenerMultiplexer pixelListeners;

  public LayerTranslator(ReadLayer innerLayer) {
    this.innerLayer = innerLayer;
    this.offset = Vector.create(0, 0);
    this.listenerMap = new HashMap<>();
    this.pixelListeners = new PixelListenerMultiplexer();
  }

  // Returns the offset for this layer.
  public Vector getOffset() {
    return offset;
  }

  // Sets the offset for this layer.
  public void setOffset(Vector offset) {
    if (this.offset.equals(offset)) {
      return;
    }

    // Notify listeners that frame contents are about to change.
    pixelListeners.onNextFrame();

    Range2 oldExtent = getExtent();
    this.offset = offset;
    Range2 newExtent = getExtent();

    pixelListeners.onPixelsChanged(oldExtent.includeBoth(newExtent));
  }

  // Adds the given offset to the current offset for this layer.
  public void addOffset(Vector offset) {
    setOffset(this.offset.add(offset));
  }

  @Override
  public Range2 getExtent() {
    return innerLayer.getExtent().shift(offset);
  }

  @Override
  public Pixel getPixel(int x, int y) {
    return innerLayer.getPixel(x - offset.dx(), y - offset.dy());
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.add(listener);

    PixelListener subListener =
        new PixelListener() {
          public void onNextFrame() {
            listener.onNextFrame();
          }

          public void onPixelChanged(Point p) {
            listener.onPixelChanged(p.add(offset));
          }

          public void onPixelsChanged(Range2 range) {
            listener.onPixelsChanged(range.shift(offset));
          }
        };
    listenerMap.put(listener, subListener);
    innerLayer.addPixelListener(subListener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.remove(listener);

    PixelListener subListener = listenerMap.remove(listener);
    if (subListener != null) {
      innerLayer.removePixelListener(subListener);
    }
  }

  @Override
  public void removeAllPixelListeners() {
    pixelListeners.clear();

    listenerMap.values().forEach(subListener -> innerLayer.removePixelListener(subListener));
    listenerMap.clear();
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    innerLayer.addCloseListener(listener);
  }
}
