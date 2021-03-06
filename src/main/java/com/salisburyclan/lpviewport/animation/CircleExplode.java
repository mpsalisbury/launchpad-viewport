package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.FramedAnimation;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.apps.Circle;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Grows a circle from a center point to the edge of the extent.
public class CircleExplode extends FramedAnimation {

  private final WriteLayer layer;
  private final Point center;
  private final Color color;

  private static final int FADE_LENGTH = 5;

  public CircleExplode(Range2 extent, Point center, Color color) {
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
    timeline.setOnFinished(
        event -> {
          layer.close();
        });
    addTimeline(timeline);

    explodeDistance.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldVal, Object newVal) {
            renderExplodeFrame((Integer) oldVal, (Integer) newVal);
          }
        });
  }

  private int getMaxDistanceToCorner() {
    Range2 extent = layer.getExtent();
    int bigX = Math.max(center.x() - extent.xRange().low(), extent.xRange().high() - center.x());
    int bigY = Math.max(center.y() - extent.yRange().low(), extent.yRange().high() - center.y());
    return bigX + bigY;
  }

  private void renderExplodeFrame(int innerDistance, int outerDistance) {
    layer.nextFrame();
    drawCircle(center, innerDistance, outerDistance);
  }

  private void drawCircle(Point center, int innerRadius, int outerRadius) {
    Circle.drawRangeCircle(layer, center, innerRadius, outerRadius, color);
  }
}
