package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
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

public class BorderSweep extends Animation {

  private LightLayer outputLayer;
  private Color color;

  public BorderSweep(LightLayer outputLayer, Color color) {
    this.outputLayer = outputLayer;
    this.color = color;
    init();
  }

  public static AnimationProvider newProvider(Color color) {
    return new AnimationProvider() {
      @Override
      public Animation newAnimation(RawViewport viewport) {
        return new BorderSweep(viewport.getLightLayer(), color);
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
            renderDots((Integer) oldLocation, Color.BLACK);
            renderDots((Integer) newLocation, color);
          }
        });
  }

  protected void renderDots(int location, Color color) {
    Range2 extent = outputLayer.getExtent();

    int x = location;
    int y = 0;
    if (x >= extent.getWidth()) {
      int diff = location - extent.getWidth() + 1;
      x -= diff;
      y += diff;
    }
    outputLayer.setLight(extent.origin().add(Vector.create(x, y)), color);

    x = 0;
    y = location;
    if (y >= extent.getHeight()) {
      int diff = location - extent.getHeight() + 1;
      x += diff;
      y -= diff;
    }
    outputLayer.setLight(extent.origin().add(Vector.create(x, y)), color);
  }
}
