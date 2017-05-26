package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Sweep extends Animation {

  private Color color;

  public Sweep(Viewport viewport, Color color, boolean forever) {
    super(viewport);
    this.color = color;
    init(forever);
  }

  public static AnimationProvider newProvider(Color color, boolean forever) {
    return new AnimationProvider() {
      @Override
      public Animation newAnimation(Viewport viewport) {
        return new Sweep(viewport, color, forever);
      }
    };
  }

  protected void init(boolean forever) {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    if (forever) {
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.setAutoReverse(true);
    }

    ViewExtent extent = getViewport().getExtent();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.ZERO, new KeyValue(barLocation, extent.getXLow(), Interpolator.EASE_BOTH)),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(barLocation, extent.getXHigh(), Interpolator.EASE_BOTH)));
    addTimeline(timeline);

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderBar((Integer) oldLocation, Color.BLACK);
            renderBar((Integer) newLocation, color);
          }
        });
  }

  protected void renderBar(int x, Color color) {
    Viewport viewport = getViewport();
    viewport
        .getExtent()
        .getYRange()
        .forEach(
            y -> {
              viewport.setLight(x, y, color);
            });
  }
}
