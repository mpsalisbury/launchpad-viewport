package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.ReadWriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Fills the entire frame with a given color, one random pixel at a time.
public class RandomFill extends AnimatedLayer {

  private final ReadWriteLayer layer;
  private final Color color;
  private final List<Point> orderedPoints;

  private static final Duration DEFAULT_DURATION = Duration.seconds(3);

  public RandomFill(Range2 extent) {
    this(extent, Color.BLACK, DEFAULT_DURATION);
  }

  public RandomFill(Range2 extent, Color color, Duration duration) {
    this.layer = new LayerBuffer(extent);
    this.color = color;
    this.orderedPoints = randomizePoints(extent);
    initAnimation(duration);
  }

  @Override
  protected ReadLayer getReadLayer() {
    return layer;
  }

  private List<Point> randomizePoints(Range2 extent) {
    List<Point> points = new ArrayList<>();
    extent.forEach((x, y) -> points.add(Point.create(x, y)));
    Collections.shuffle(points);
    return points;
  }

  private void initAnimation(Duration duration) {
    IntegerProperty step = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(step, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(step, orderedPoints.size())));
    timeline.setOnFinished(event -> fireOnFinished());
    addTimeline(timeline);

    step.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldStep, Object newStep) {
            renderPixel((Integer) oldStep, (Integer) newStep);
          }
        });
  }

  private void renderPixel(int fromStepInclusive, int toStepExclusive) {
    for (int i = fromStepInclusive; i < toStepExclusive; ++i) {
      layer.setPixel(orderedPoints.get(i), color);
    }
  }
}
