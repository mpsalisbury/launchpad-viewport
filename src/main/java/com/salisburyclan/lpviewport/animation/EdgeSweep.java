package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.viewport.Edge;
import com.salisburyclan.lpviewport.viewport.Point;
import com.salisburyclan.lpviewport.viewport.Range;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class EdgeSweep extends Animation {

  private Edge edge;
  private Color color;

  public EdgeSweep(Viewport viewport, Edge edge, Color color) {
    super(viewport);
    this.edge = edge;
    this.color = color;
    init();
  }

  protected void init() {
    IntegerProperty dotLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.setAutoReverse(true);

    ViewExtent extent = getViewport().getExtent();
    Range range = edge.getRange(extent);
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.ZERO, new KeyValue(dotLocation, range.low)),
        new KeyFrame(Duration.seconds(1), new KeyValue(dotLocation, range.high)));
    addTimeline(timeline);

    dotLocation.addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
        renderDot((Integer)oldLocation, Color.BLACK);
        renderDot((Integer)newLocation, color);
      }
    });
  }

  protected void renderDot(int location, Color color) {
    Viewport viewport = getViewport();
    Point point = edge.getPoint(viewport.getExtent(), location);
    viewport.setLight(point.x, point.y, color);
  }
}
