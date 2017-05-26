package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class BorderSweep extends Animation {

  private Color color;

  public BorderSweep(Viewport viewport, Color color) {
    super(viewport);
    this.color = color;
    init();
  }

  public static AnimationProvider newProvider(Color color) {
    return new AnimationProvider() {
      @Override
      public Animation newAnimation(Viewport viewport) {
        return new BorderSweep(viewport, color);
      }
    };
  }

  protected void init() {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.setAutoReverse(true);

    ViewExtent extent = getViewport().getExtent();
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.ZERO, new KeyValue(barLocation, 0)),
        new KeyFrame(Duration.seconds(1),
          new KeyValue(barLocation, extent.getWidth() + extent.getHeight())));
    addTimeline(timeline);

    barLocation.addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
        renderDots((Integer)oldLocation, Color.BLACK);
        renderDots((Integer)newLocation, color);
      }
    });
  }

  protected void renderDots(int location, Color color) {
    Viewport viewport = getViewport();
    ViewExtent extent = viewport.getExtent();

    int x = location;
    int y = 0;
    if (x >= extent.getWidth()) {
      int diff = location - extent.getWidth() + 1;
      x -= diff;
      y += diff;
    }
    viewport.setLight(x + extent.getXLow(), y + extent.getYLow(), color);

    x = 0;
    y = location;
    if (y >= extent.getHeight()) {
      int diff = location - extent.getHeight() + 1;
      x += diff;
      y -= diff;
    }
    viewport.setLight(x + extent.getXLow(), y + extent.getYLow(), color);
  }
}
