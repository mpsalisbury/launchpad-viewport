package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.stream.IntStream;

public class Sweep extends JavafxLaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    viewport = getViewport();
    sweep(Color.RED);
  }

  private void sweep(Color color) {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.setAutoReverse(true);
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.ZERO, new KeyValue(barLocation, viewport.getExtent().getXLow(), Interpolator.EASE_BOTH)),
        new KeyFrame(Duration.seconds(1), new KeyValue(barLocation, viewport.getExtent().getXHigh(), Interpolator.EASE_BOTH)));
    timeline.play();

    barLocation.addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
        renderBar((Integer)oldLocation, Color.BLACK);
        renderBar((Integer)newLocation, color);
      }
    });
  }

  public void renderBar(int x, Color color) {
    viewport.getExtent().getYRange().forEach(y -> {
      viewport.setLight(x, y, color);
    });
  }
}
