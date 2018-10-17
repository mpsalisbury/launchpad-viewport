package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.FramedAnimation;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Explodes a diamond from a centerpoint through an extent.
public class Explode extends FramedAnimation {

  private final WriteLayer layer;
  private final Point center;
  private final Color color;

  public Explode(Range2 extent, Point center, Color color) {
    super(extent);
    this.layer = getWriteLayer();
    this.center = center;
    this.color = color;
    init();
  }

  private void init() {
    final int maxDistance = getMaxDistanceToCorner() + 1;
    IntegerProperty explodeDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(explodeDistance, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(explodeDistance, maxDistance)));
    timeline.setOnFinished(
        event -> {
          layer.close();
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
    Range2 extent = layer.getExtent();
    int bigX = Math.max(center.x() - extent.xRange().low(), extent.xRange().high() - center.x());
    int bigY = Math.max(center.y() - extent.yRange().low(), extent.yRange().high() - center.y());
    return bigX + bigY + 1;
  }

  private void renderExplodeFrame(int radius) {
    layer.nextFrame();
    for (int pos = 0; pos <= radius; pos++) {
      layer.setPixel(center.add(Vector.create(pos, radius - pos)), color);
      layer.setPixel(center.add(Vector.create(pos, -(radius - pos))), color);
      layer.setPixel(center.add(Vector.create(-pos, radius - pos)), color);
      layer.setPixel(center.add(Vector.create(-pos, -(radius - pos))), color);
    }
  }
}
