package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.DecayingBuffer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Explode extends DecayingAnimation {

  private final DecayingBuffer buffer;
  private final Point center;
  private final DColor color;

  private static final int FADE_LENGTH = 5;

  public Explode(Range2 extent, Point center, Color color) {
    super(extent);
    this.buffer = getBuffer();
    this.center = center;
    this.color = DColor.create(color);
    init();
  }

  private void init() {
    final int maxDistance = getMaxDistanceToCorner() + FADE_LENGTH + 1;
    IntegerProperty explodeDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(explodeDistance, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(explodeDistance, maxDistance)));
    timeline.setOnFinished(
        event -> {
          buffer.cleanUp();
        });
    addTimeline(timeline);

    explodeDistance.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object distance, Object newVal) {
            renderExplodeFrame((Integer) distance);
          }
        });
  }

  private int getMaxDistanceToCorner() {
    Range2 extent = buffer.getExtent();
    int bigX = Math.max(center.x() - extent.xRange().low(), extent.xRange().high() - center.x());
    int bigY = Math.max(center.y() - extent.yRange().low(), extent.yRange().high() - center.y());
    return bigX + bigY;
  }

  private void renderExplodeFrame(int distance) {
    buffer.pushFrame();
    for (int pos = 0; pos <= distance; pos++) {
      buffer.setPixel(center.add(Vector.create(pos, distance - pos)), color);
      buffer.setPixel(center.add(Vector.create(pos, -(distance - pos))), color);
      buffer.setPixel(center.add(Vector.create(-pos, distance - pos)), color);
      buffer.setPixel(center.add(Vector.create(-pos, -(distance - pos))), color);
    }
  }
}
