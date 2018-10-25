package com.salisburyclan.lpviewport.geom.scaler;

import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.scaler.Rounder.RoundingPolicy;

public class Range1Scaler {
  private FloatRange1 inputRange;
  private FloatRange1 outputRange;

  public Range1Scaler(Range1 inputRange, Range1 outputRange) {
    this.inputRange = FloatRange1.create(inputRange);
    this.outputRange = FloatRange1.create(outputRange);
  }

  private Range1Scaler(FloatRange1 inputRange, FloatRange1 outputRange) {
    this.inputRange = inputRange;
    this.outputRange = outputRange;
  }

  public Range1Scaler invert() {
    return new Range1Scaler(outputRange, inputRange);
  }

  // Returns the output position corresponding to the given input position, rounding to nearest
  // location.
  public int mapToInt(int inputPosition) {
    return Rounder.round(mapToFloat(inputPosition), RoundingPolicy.ROUND_CLOSEST);
  }

  // Maps pixel (range) named by source coords to corresponding FloatRange2.
  public FloatRange1 mapPixel(int position) {
    return mapToFloatRange1(Range1.create(position, position));
  }

  public Range1 mapToRange1(Range1 input) {
    return roundToRange1(mapToFloatRange1(input));
  }

  // Returns the output 1-d position (x-dimension) corresponding to the given source 1-d position.
  private float mapToFloat(float inputPosition) {
    float outputPosition =
        (inputPosition - inputRange.low()) * outputRange.size() / inputRange.size()
            + outputRange.low();
    return outputPosition;
  }

  // Returns the output Range1 corresponding to the given input range.
  public FloatRange1 mapToFloatRange1(Range1 input) {
    return mapToFloatRange1(FloatRange1.create(input));
  }

  public FloatRange1 mapToFloatRange1(FloatRange1 input) {
    float low = mapToFloat(input.low());
    float high = mapToFloat(input.high());
    return FloatRange1.create(low, high);
  }

  public Range1 roundToRange1(FloatRange1 range) {
    int low = Rounder.round(range.low(), RoundingPolicy.ROUND_DOWN);
    int high = Rounder.round(range.high(), RoundingPolicy.ROUND_UP);
    return Range1.create(low, high);
  }
}
