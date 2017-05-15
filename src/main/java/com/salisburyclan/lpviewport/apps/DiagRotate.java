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

public class DiagRotate extends JavafxLaunchpadApplication {

  private static final Color BOX_COLOR = Color.BLUE;
  private static final Color BAR_COLOR = Color.ORANGE;
  private Viewport viewport;
  private ViewExtent barExtent;

  @Override
  public void run() {
    viewport = getViewport();
    barExtent = viewport.getExtent().inset(1,1,1,1);
    rotate();
  }

  private void rotate() {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().addAll(
//        new KeyFrame(Duration.ZERO, new KeyValue(barLocation, 0)),
        new KeyFrame(Duration.seconds(1), new KeyValue(barLocation, 3)));
    timeline.play();

    renderBox();
    barLocation.addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
        renderBars((Integer)newLocation, BAR_COLOR);
        renderBars((Integer)oldLocation, Color.BLACK);
      }
    });
  }

  private void renderBox() {
    ViewExtent extent = viewport.getExtent();
    extent.getXRange().forEach(x -> {
      viewport.setLight(x, extent.getYLow(), BOX_COLOR);
      viewport.setLight(x, extent.getYHigh(), BOX_COLOR);
    });
    extent.getYRange().forEach(y -> {
      viewport.setLight(extent.getXLow(), y, BOX_COLOR);
      viewport.setLight(extent.getXHigh(), y, BOX_COLOR);
    });
  }

  private void renderBars(int linePosition, Color color) {
    for (int x = linePosition; x < barExtent.getWidth() + barExtent.getHeight(); x += 4) {
      renderBar(x, color);
    }
  }

  private void renderBar(int linePosition, Color color) {
    int x1 = barExtent.getXLow();
    int y1 = barExtent.getYLow() + linePosition;
    int x2 = barExtent.getXLow() + linePosition;
    int y2 = barExtent.getYLow();
    if (y1 > barExtent.getYHigh()) {
      int overshoot = y1 - barExtent.getYHigh();
      y1 -= overshoot;
      x1 += overshoot;
    }
    if (x2 > barExtent.getXHigh()) {
      int overshoot = x2 - barExtent.getXHigh();
      x2 -= overshoot;
      y2 += overshoot;
    }
    for (int x = x1, y = y1; x <= x2; ++x, --y) {
      viewport.setLight(x, y, color);
    }
  }
}
