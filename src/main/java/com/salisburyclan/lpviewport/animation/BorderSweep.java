package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.layer.FrameWriteLayer;
import com.salisburyclan.lpviewport.layer.Pixel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class BorderSweep extends FramedAnimation {
  private FrameWriteLayer outputLayer;
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
      public FramedAnimation newAnimation(Range2 extent) {
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
                new KeyValue(barLocation, extent.getWidth() + extent.getHeight() - 2)));
    addTimeline(timeline);

    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            //            renderDots((Integer) oldLocation, Pixel.EMPTY);
            renderDots((Integer) newLocation, pixel);
          }
        });
  }

  protected void renderDots(int location, Pixel pixel) {
    outputLayer.nextFrame();
    Range2 extent = outputLayer.getExtent();

    int x = location;
    int y = 0;
    if (x >= extent.getWidth()) {
      int diff = location - extent.getWidth() + 1;
      x -= diff;
      y += diff;
    }
    outputLayer.setPixel(extent.origin().add(Vector.create(x, y)), pixel);

    x = 0;
    y = location;
    if (y >= extent.getHeight()) {
      int diff = location - extent.getHeight() + 1;
      x += diff;
      y -= diff;
    }
    outputLayer.setPixel(extent.origin().add(Vector.create(x, y)), pixel);
  }
}
