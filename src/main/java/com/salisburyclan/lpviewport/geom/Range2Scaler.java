package com.salisburyclan.lpviewport.geom;

import com.salisburyclan.lpviewport.geom.scaler.FloatRange1;
import com.salisburyclan.lpviewport.geom.scaler.FloatRange2;
import com.salisburyclan.lpviewport.geom.scaler.Range1Scaler;

// Provides transforms for converting an input Range2 to an output Range2.
// Transforms are performed in continuous (float) space and can be rounded
// to map back into discrete (int) space.
public class Range2Scaler {
  private Range1Scaler xScaler;
  private Range1Scaler yScaler;

  public Range2Scaler(Range2 inputRange, Range2 outputRange) {
    this.xScaler = new Range1Scaler(inputRange.xRange(), outputRange.xRange());
    this.yScaler = new Range1Scaler(inputRange.yRange(), outputRange.yRange());
  }

  private Range2Scaler(Range1Scaler xScaler, Range1Scaler yScaler) {
    this.xScaler = xScaler;
    this.yScaler = yScaler;
  }

  public Range2Scaler invert() {
    return new Range2Scaler(xScaler.invert(), yScaler.invert());
  }

  // Returns the output Point corresponding to the given source Point, rounding to nearest location.
  public Point mapToPoint(Point source) {
    int x = xScaler.mapToInt(source.x());
    int y = yScaler.mapToInt(source.y());
    return Point.create(x, y);
  }

  public Range2 mapToRange2(Range2 source) {
    return mapToFloatRange2(source).round();
  }

  @FunctionalInterface
  public interface WeightedPointCallback {
    void apply(int x, int y, float weight);
  }

  // For each int range in this float range, callback with the Point and a weighted coverage
  // representing how much of the Point range is covered by the float range.
  public void mapToWeightedIterator(Range2 source, WeightedPointCallback callback) {
    FloatRange2 mappedRange = mapToFloatRange2(source);
    mappedRange.weightedIterator(callback);
  }

  private FloatRange2 mapToFloatRange2(Range2 source) {
    FloatRange1 xRange = xScaler.mapToFloatRange1(source.xRange());
    FloatRange1 yRange = yScaler.mapToFloatRange1(source.yRange());
    return FloatRange2.create(xRange, yRange);
  }

  private FloatRange2 mapToFloatRange2(FloatRange2 source) {
    FloatRange1 xRange = xScaler.mapToFloatRange1(source.xRange());
    FloatRange1 yRange = yScaler.mapToFloatRange1(source.yRange());
    return FloatRange2.create(xRange, yRange);
  }
}
