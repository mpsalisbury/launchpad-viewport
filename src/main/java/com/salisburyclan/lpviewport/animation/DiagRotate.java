package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.FramedAnimation;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Sweep diagonal lines through an extent.
public class DiagRotate extends FramedAnimation {

  private final WriteLayer layer;
  private final Color color;

  public DiagRotate(Range2 extent, Color color) {
    super(extent);
    this.layer = getWriteLayer();
    this.color = color;
    init();
  }

  protected void init() {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline
        .getKeyFrames()
        .addAll(
            //        new KeyFrame(Duration.ZERO, new KeyValue(barLocation, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(barLocation, 3)));
    addTimeline(timeline);

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderBars((Integer) newLocation);
          }
        });
  }

  private void renderBars(int linePosition) {
    layer.nextFrame();
    Range2 extent = getExtent();
    for (int x = linePosition; x < extent.width() + extent.height(); x += 4) {
      renderBar(x);
    }
  }

  private void renderBar(int linePosition) {
    Range2 extent = getExtent();
    int x1 = extent.xRange().low();
    int y1 = extent.yRange().low() + linePosition;
    int x2 = extent.xRange().low() + linePosition;
    int y2 = extent.yRange().low();
    if (y1 > extent.yRange().high()) {
      int overshoot = y1 - extent.yRange().high();
      y1 -= overshoot;
      x1 += overshoot;
    }
    if (x2 > extent.xRange().high()) {
      int overshoot = x2 - extent.xRange().high();
      x2 -= overshoot;
      y2 += overshoot;
    }
    for (int x = x1, y = y1; x <= x2; ++x, --y) {
      layer.setPixel(x, y, color);
    }
  }
}
