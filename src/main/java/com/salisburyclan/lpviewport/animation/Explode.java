package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Explode extends Animation {

  private final int centerX;
  private final int centerY;
  private final Color color;

  private static final int FADE_LENGTH = 5;

  public Explode(Viewport viewport, int centerX, int centerY, Color color) {
    super(viewport);
    this.centerX = centerX;
    this.centerY = centerY;
    this.color = color;
    init();
  }

  private void init() {
    final int maxDistance = getMaxDistanceToCorner() + FADE_LENGTH + 1;
    IntegerProperty explodeDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(explodeDistance, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(explodeDistance, maxDistance)));
    addTimeline(timeline);

    explodeDistance.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object distance, Object newVal) {
            renderExplodeFrame((Integer) distance);
          }
        });
  }

  private int getMaxDistanceToCorner() {
    ViewExtent extent = getViewport().getExtent();
    int bigX = Math.max(centerX - extent.getXLow(), extent.getXHigh() - centerX);
    int bigY = Math.max(centerY - extent.getYLow(), extent.getYHigh() - centerY);
    return bigX + bigY;
  }

  private void renderExplodeFrame(int distance) {
    for (int fade = 0; fade <= FADE_LENGTH; fade++) {
      Color moderatedColor = moderateColor(FADE_LENGTH - fade, FADE_LENGTH, color);
      renderDiamond(distance - fade, moderatedColor);
    }
  }

  private void renderDiamond(int radius, Color color) {
    Viewport viewport = getViewport();
    for (int pos = 0; pos <= radius; pos++) {
      viewport.setLight(centerX + pos, centerY + (radius - pos), color);
      viewport.setLight(centerX + pos, centerY - (radius - pos), color);
      viewport.setLight(centerX - pos, centerY + (radius - pos), color);
      viewport.setLight(centerX - pos, centerY - (radius - pos), color);
    }
  }

  private Color moderateColor(int numerator, int denominator, Color color) {
    double percent = (double) numerator / denominator;
    if (percent > 1.0) {
      percent = 1.0;
    }
    if (percent < 0.0) {
      percent = 0.0;
    }
    return Color.of(
        (int) (color.getRed() * percent),
        (int) (color.getGreen() * percent),
        (int) (color.getBlue() * percent));
  }
}
