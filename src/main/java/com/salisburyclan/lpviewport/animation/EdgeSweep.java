package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.FrameWriteLayer;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class EdgeSweep extends FramedAnimation {

  private FrameWriteLayer writeLayer;
  private Edge edge;
  private Pixel pixel;

  public EdgeSweep(Range2 extent, Edge edge, Color color) {
    super(extent);
    this.writeLayer = getWriteLayer();
    this.edge = edge;
    this.pixel = Pixel.create(color);
    init();
  }

  protected void init() {
    IntegerProperty dotLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.setAutoReverse(true);

    Range2 extent = writeLayer.getExtent();
    Range1 range = edge.getRange(extent);
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(dotLocation, range.low())),
            new KeyFrame(Duration.seconds(1), new KeyValue(dotLocation, range.high())));
    addTimeline(timeline);

    dotLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderDot((Integer) newLocation);
          }
        });
  }

  protected void renderDot(int location) {
    writeLayer.nextFrame();
    Point point = edge.getPoint(writeLayer.getExtent(), location);
    writeLayer.setPixel(point, pixel);
  }
}
