package com.salisburyclan.lpviewport.geom.scaler;

import static com.google.common.truth.Truth.assertThat;

import com.salisburyclan.lpviewport.geom.testing.WeightedPoint;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FloatRange2Test {

  // Collect and return weightedIterator callback values.
  private Set<WeightedPoint> collectWeights(float xLow, float yLow, float xHigh, float yHigh) {
    FloatRange2 range = FloatRange2.create(xLow, yLow, xHigh, yHigh);
    Set<WeightedPoint> weights = new HashSet<>();
    range.weightedIterator((x, y, weight) -> weights.add(WeightedPoint.create(x, y, weight)));
    return weights;
  }

  @Test
  public void testWeightedIterator_empty() {
    Set<WeightedPoint> weights = collectWeights(0.0f, 1.0f, 0.0f, 2.0f);
    assertThat(weights).isEmpty();
  }

  @Test
  public void testWeightedIterator_unit() {
    Set<WeightedPoint> weights = collectWeights(0.0f, 0.0f, 1.0f, 1.0f);
    Set<WeightedPoint> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPoint.create(0, 0, 1.0));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_multipleWhole() {
    Set<WeightedPoint> weights = collectWeights(0.0f, 1.0f, 2.0f, 3.0f);
    Set<WeightedPoint> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPoint.create(0, 1, 1.0));
    expectedWeights.add(WeightedPoint.create(0, 2, 1.0));
    expectedWeights.add(WeightedPoint.create(1, 1, 1.0));
    expectedWeights.add(WeightedPoint.create(1, 2, 1.0));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialSingle() {
    Set<WeightedPoint> weights = collectWeights(0.2f, 1.1f, 0.7f, 1.6f);
    Set<WeightedPoint> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPoint.create(0, 1, 0.25));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialMultiple() {
    Set<WeightedPoint> weights = collectWeights(0.2f, 1.1f, 2.7f, 1.6f);
    Set<WeightedPoint> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPoint.create(0, 1, 0.4));
    expectedWeights.add(WeightedPoint.create(1, 1, 0.5));
    expectedWeights.add(WeightedPoint.create(2, 1, 0.35));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }
}
