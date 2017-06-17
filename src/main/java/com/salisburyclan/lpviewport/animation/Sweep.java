package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.AnimatedLayer;
import com.salisburyclan.lpviewport.layer.Pixel;
import com.salisburyclan.lpviewport.layer.WriteLayer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Sweep extends AnimatedLayer {
  private WriteLayer layer;
  private Pixel pixel;

  public Sweep(Range2 extent, Color color, boolean forever) {
    super(extent);
    this.layer = getWriteLayer();
    this.pixel = Pixel.create(color);
    init(forever);
  }

  public static AnimationProvider newProvider(Color color, boolean forever) {
    return new AnimationProvider() {
      @Override
      public AnimatedLayer newAnimation(Range2 extent) {
        return new Sweep(extent, color, forever);
      }
    };
  }

  public void stop() {
    layer.close();
    super.stop();
  }

  protected void init(boolean forever) {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    if (forever) {
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.setAutoReverse(true);
    }

    Range2 extent = layer.getExtent();
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
    // TODO stop on done.

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderBar((Integer) oldLocation, Pixel.EMPTY);
            renderBar((Integer) newLocation, pixel);
          }
        });
  }

  protected void renderBar(int x, Pixel pixel) {
    layer
        .getExtent()
        .yRange()
        .stream()
        .forEach(
            y -> {
              layer.setPixel(x, y, pixel);
            });
  }
}
