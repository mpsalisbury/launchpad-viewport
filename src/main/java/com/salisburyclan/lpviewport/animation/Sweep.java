package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
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
  private Viewport viewport;
  private Color color;

  public Sweep(Viewport viewport, Color color, boolean forever) {
    this.viewport = viewport;
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

    Range2 extent = viewport.getExtent();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(barLocation, extent.xRange().low(), Interpolator.EASE_BOTH)),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(barLocation, extent.xRange().high(), Interpolator.EASE_BOTH)));
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
    viewport
        .getExtent()
        .yRange()
        .stream()
        .forEach(
            y -> {
              viewport.setLight(x, y, color);
            });
  }
}
