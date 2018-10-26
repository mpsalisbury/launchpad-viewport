package com.salisburyclan.lpviewport.api;

import com.google.common.util.concurrent.AtomicDouble;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Range2Scaler;

// Scales a layer to another size.
public class LayerScaler extends ReadLayerProxy {
  private ReadLayer sourceLayer;
  private ReadWriteLayer outputLayer;
  private Range2Scaler sourceToOutputScaler;

  public LayerScaler(ReadLayer sourceLayer, Range2 outputExtent) {
    this.sourceLayer = sourceLayer;
    this.outputLayer = new LayerBuffer(outputExtent);
    this.sourceToOutputScaler = new Range2Scaler(sourceLayer.getExtent(), outputLayer.getExtent());

    addSourceListener();
    recomputeForSourcePixels(sourceLayer.getExtent());
  }

  private void addSourceListener() {
    sourceLayer.addPixelListener(
        new PixelListener() {
          @Override
          public void onPixelChanged(Point p) {
            recomputeForSourcePixels(Range2.create(p));
          }

          @Override
          public void onPixelsChanged(Range2 range) {
            recomputeForSourcePixels(range);
          }
        });
  }

  @Override
  public ReadLayer getReadLayer() {
    return outputLayer;
  }

  public Range2Scaler getScaler() {
    return sourceToOutputScaler;
  }

  // Recomputes the output pixels affected by the given sourceRange.
  private void recomputeForSourcePixels(Range2 sourceRange) {
    Range2 outputRange = sourceToOutputScaler.mapToRange2(sourceRange);
    outputRange.forEach((x, y) -> outputLayer.setPixel(x, y, recomputeOutputPixel(x, y)));
  }

  // Returns the given output pixel from the corresponding source layer region.
  private Pixel recomputeOutputPixel(int x, int y) {
    AtomicDouble totalRed = new AtomicDouble(0.0);
    AtomicDouble totalGreen = new AtomicDouble(0.0);
    AtomicDouble totalBlue = new AtomicDouble(0.0);
    AtomicDouble totalAlpha = new AtomicDouble(0.0);
    AtomicDouble totalWeight = new AtomicDouble(0.0);

    Range2 outputRange = Range2.create(Point.create(x, y));
    sourceToOutputScaler
        .invert()
        .mapToWeightedIterator(
            outputRange,
            (wx, wy, weight) -> {
              // collect together weighted pixel values.
              Pixel pixel = sourceLayer.getPixel(wx, wy);

              totalRed.addAndGet(pixel.color().red() * weight);
              totalGreen.addAndGet(pixel.color().green() * weight);
              totalBlue.addAndGet(pixel.color().blue() * weight);
              totalAlpha.addAndGet(pixel.alpha() * weight);
              totalWeight.addAndGet(weight);
            });

    if (totalWeight.doubleValue() == 0.0) {
      return Pixel.EMPTY;
    }
    return Pixel.create(
        Color.create(
            totalRed.doubleValue() / totalWeight.doubleValue(),
            totalGreen.doubleValue() / totalWeight.doubleValue(),
            totalBlue.doubleValue() / totalWeight.doubleValue()),
        totalAlpha.doubleValue() / totalWeight.doubleValue());
  }
}
