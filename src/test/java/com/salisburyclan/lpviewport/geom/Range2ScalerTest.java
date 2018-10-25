package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import com.salisburyclan.lpviewport.geom.testing.WeightedPoint;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Range2ScalerTest {

  @Test
  public void testMapToPoint() {
    Range2 inputRange = Range2.create(0, 0, 10, 10);
    Range2 outputRange = Range2.create(0, 0, 100, 100);
    Range2Scaler scaler = new Range2Scaler(inputRange, outputRange);

    assertMapToPoint(scaler, 0, 0, 4, 4);
    assertMapToPoint(scaler, 10, 0, 96, 4);
    assertMapToPoint(scaler, 10, 10, 96, 96);
    assertMapToPoint(scaler, 0, 10, 4, 96);

    assertMapToPoint(scaler, 5, 5, 50, 50);
  }

  @Test
  public void testInvert() {
    Range2 inputRange = Range2.create(0, 0, 10, 10);
    Range2 outputRange = Range2.create(0, 0, 100, 100);
    Range2Scaler scaler = new Range2Scaler(inputRange, outputRange).invert();

    assertMapToPoint(scaler, 4, 4, 0, 0);
    assertMapToPoint(scaler, 96, 4, 10, 0);
    assertMapToPoint(scaler, 96, 96, 10, 10);
    assertMapToPoint(scaler, 4, 96, 0, 10);

    assertMapToPoint(scaler, 50, 50, 5, 5);
  }

  private void assertMapToPoint(Range2Scaler scaler, int xFrom, int yFrom, int xTo, int yTo) {
    Point from = Point.create(xFrom, yFrom);
    Point expectedTo = Point.create(xTo, yTo);
    assertThat(scaler.mapToPoint(from)).isEqualTo(expectedTo);
  }

  @Test
  public void testMapToWeightedIterator() {
    Range2 inputRange = Range2.create(0, 0, 9, 9);
    Range2 outputRange = Range2.create(0, 0, 19, 19);
    Range2Scaler scaler = new Range2Scaler(inputRange, outputRange);

    List<WeightedPoint> points = new ArrayList<>();
    scaler.mapToWeightedIterator(
        Range2.create(Point.create(0, 0)),
        (x, y, weight) -> {
          points.add(WeightedPoint.create(x, y, weight));
        });

    List<WeightedPoint> expectedPoints = new ArrayList<>();
    expectedPoints.add(WeightedPoint.create(-1, -1, 0.25));
    expectedPoints.add(WeightedPoint.create(-1, -0, 0.5));
    expectedPoints.add(WeightedPoint.create(-1, 1, 0.25));
    expectedPoints.add(WeightedPoint.create(0, -1, 0.5));
    expectedPoints.add(WeightedPoint.create(0, 0, 1.0));
    expectedPoints.add(WeightedPoint.create(0, 1, 0.5));
    expectedPoints.add(WeightedPoint.create(1, -1, 0.25));
    expectedPoints.add(WeightedPoint.create(1, 0, 0.5));
    expectedPoints.add(WeightedPoint.create(1, 1, 0.25));

    assertThat(points).containsExactlyElementsIn(expectedPoints);
  }
}
