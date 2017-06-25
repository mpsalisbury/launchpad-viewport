package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.FrameWriteLayer;
import com.salisburyclan.lpviewport.layer.Pixel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Sweep extends FramedAnimation {
  private FrameWriteLayer layer;
  private Pixel pixel;

  public Sweep(Range2 extent, DColor color, boolean forever) {
    super(extent);
    this.layer = getWriteLayer();
    this.pixel = Pixel.create(color);
    init(forever);
  }

  public static AnimationProvider newProvider(DColor color, boolean forever) {
    return new AnimationProvider() {
      @Override
      public AnimatedLayer newAnimation(Range2 extent) {
        return new Sweep(extent, color, forever);
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
    timeline.setOnFinished(
        event -> {
          layer.close();
          this.stop();
        });
    addTimeline(timeline);

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderBar((Integer) newLocation);
          }
        });
  }

  protected void renderBar(int x) {
    layer.nextFrame();
    layer
        .getExtent()
        .yRange()
        .forEach(
            y -> {
              layer.setPixel(x, y, pixel);
            });
  }
}
