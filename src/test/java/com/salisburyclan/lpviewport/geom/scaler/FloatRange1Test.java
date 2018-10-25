package com.salisburyclan.lpviewport.geom.scaler;

import static com.google.common.truth.Truth.assertThat;

import com.salisburyclan.lpviewport.geom.testing.WeightedPosition;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FloatRange1Test {

  // Collect and return weightedIterator callback values.
  private Set<WeightedPosition> collectWeights(float low, float high) {
    FloatRange1 range = FloatRange1.create(low, high);
    Set<WeightedPosition> weights = new HashSet<>();
    range.weightedIterator((x, weight) -> weights.add(WeightedPosition.create(x, weight)));
    return weights;
  }

  @Test
  public void testWeightedIterator_empty() {
    Set<WeightedPosition> weights = collectWeights(1.0f, 1.0f);
    assertThat(weights).isEmpty();
  }

  @Test
  public void testWeightedIterator_unit() {
    Set<WeightedPosition> weights = collectWeights(0.0f, 1.0f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(0, 1.0));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_multipleWhole() {
    Set<WeightedPosition> weights = collectWeights(1.0f, 4.0f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(1, 1.0));
    expectedWeights.add(WeightedPosition.create(2, 1.0));
    expectedWeights.add(WeightedPosition.create(3, 1.0));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialSingle() {
    Set<WeightedPosition> weights = collectWeights(0.2f, 0.5f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(0, 0.3));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialLeadingEdge() {
    Set<WeightedPosition> weights = collectWeights(0.0f, 0.6f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(0, 0.6));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialTrailingEdge() {
    Set<WeightedPosition> weights = collectWeights(0.2f, 1.0f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(0, 0.8));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialMultiple() {
    Set<WeightedPosition> weights = collectWeights(0.2f, 2.5f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(0, 0.8));
    expectedWeights.add(WeightedPosition.create(1, 1.0));
    expectedWeights.add(WeightedPosition.create(2, 0.5));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }

  @Test
  public void testWeightedIterator_partialNegative() {
    Set<WeightedPosition> weights = collectWeights(-1.2f, 0.5f);
    Set<WeightedPosition> expectedWeights = new HashSet<>();
    expectedWeights.add(WeightedPosition.create(-2, 0.2));
    expectedWeights.add(WeightedPosition.create(-1, 1.0));
    expectedWeights.add(WeightedPosition.create(0, 0.5));
    assertThat(weights).containsExactlyElementsIn(expectedWeights);
  }
}
