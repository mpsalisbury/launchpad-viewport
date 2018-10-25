package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.AnimationProvider;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.FramedAnimation;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Sweeps pixels around the edges of an extent.
public class BorderSweep extends FramedAnimation {
  private WriteLayer outputLayer;
  private Pixel pixel;

  public BorderSweep(Range2 extent, Color color) {
    super(extent);
    this.outputLayer = getWriteLayer();
    this.pixel = Pixel.create(color);
    init();
  }

  public static AnimationProvider newProvider(Color color) {
    return new AnimationProvider() {
      @Override
      public AnimatedLayer newAnimation(Range2 extent) {
        return new BorderSweep(extent, color);
      }
    };
  }

  protected void init() {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.setAutoReverse(true);

    Range2 extent = outputLayer.getExtent();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(barLocation, 0)),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(barLocation, extent.width() + extent.height() - 2)));
    addTimeline(timeline);

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderDots((Integer) newLocation);
          }
        });
  }

  protected void renderDots(int location) {
    outputLayer.nextFrame();
    Range2 extent = outputLayer.getExtent();

    int x = location;
    int y = 0;
    if (x >= extent.width()) {
      int diff = location - extent.width() + 1;
      x -= diff;
      y += diff;
    }
    outputLayer.setPixel(extent.low().add(Vector.create(x, y)), pixel);

    x = 0;
    y = location;
    if (y >= extent.height()) {
      int diff = location - extent.height() + 1;
      x += diff;
      y -= diff;
    }
    outputLayer.setPixel(extent.low().add(Vector.create(x, y)), pixel);
  }
}
