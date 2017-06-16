package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.LayerBuffer;
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

public class Sweep extends Animation {
  private LayerBuffer buffer;
  private Pixel pixel;

  public Sweep(LayerBuffer buffer, Color color, boolean forever) {
    this.buffer = buffer;
    this.pixel = Pixel.create(color);
    init(forever);
  }

  public static AnimationProvider newProvider(Color color, boolean forever) {
    return new AnimationProvider() {
      @Override
      public Animation newAnimation(RawViewport rawViewport) {
        Viewport viewport = new Viewport(rawViewport);
        LayerBuffer buffer = viewport.addLayer();
        return new Sweep(buffer, color, forever);
      }
    };
  }

  public void stop() {
    buffer.close();
    super.stop();
  }

  protected void init(boolean forever) {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    if (forever) {
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.setAutoReverse(true);
    }

    Range2 extent = buffer.getExtent();
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
            renderBar((Integer) oldLocation, Pixel.EMPTY);
            renderBar((Integer) newLocation, pixel);
          }
        });
  }

  protected void renderBar(int x, Pixel pixel) {
    buffer
        .getExtent()
        .yRange()
        .stream()
        .forEach(
            y -> {
              buffer.setPixel(x, y, pixel);
            });
  }
}
