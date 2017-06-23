package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.FrameWriteLayer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Explode extends FramedAnimation {

  private final FrameWriteLayer layer;
  private final Point center;
  private final DColor color;

  private static final int FADE_LENGTH = 5;

  public Explode(Range2 extent, Point center, DColor color) {
    super(extent);
    this.layer = getWriteLayer();
    this.center = center;
    this.color = color;
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
    Range2 extent = layer.getExtent();
    int bigX = Math.max(center.x() - extent.xRange().low(), extent.xRange().high() - center.x());
    int bigY = Math.max(center.y() - extent.yRange().low(), extent.yRange().high() - center.y());
    return bigX + bigY;
  }

  private void renderExplodeFrame(int radius) {
    for (int pos = 0; pos <= radius; pos++) {
      layer.setPixel(center.add(Vector.create(pos, radius - pos)), color);
      layer.setPixel(center.add(Vector.create(pos, -(radius - pos))), color);
      layer.setPixel(center.add(Vector.create(-pos, radius - pos)), color);
      layer.setPixel(center.add(Vector.create(-pos, -(radius - pos))), color);
    }
  }
}
