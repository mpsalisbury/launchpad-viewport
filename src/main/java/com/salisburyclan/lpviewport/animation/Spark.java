package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Spark extends Animation {

  private final int centerX;
  private final int centerY;
  private final Color color;

  private static final int TAIL_LENGTH = 5;

  public Spark(Viewport viewport, int centerX, int centerY, Color color) {
    super(viewport);
    this.centerX = centerX;
    this.centerY = centerY;
    this.color = color;
    init();
  }

  // Shoots spark from center of viewport.
  public Spark(Viewport viewport, Color color) {
    super(viewport);
    ViewExtent extent = viewport.getExtent();
    this.centerX = (extent.getXLow() + extent.getXHigh()) / 2;
    this.centerY = (extent.getYLow() + extent.getYHigh()) / 2;
    this.color = color;
  }

  // Shoots spark from center of viewport.
  public static AnimationProvider newProvider(Color color) {
    return new AnimationProvider() {
      @Override
      public Animation newAnimation(Viewport viewport) {
        return new Spark(viewport, color);
      }
    };
  }

  protected void init() {
    final int maxDistance = getMaxDistanceToEdge() + TAIL_LENGTH + 1;
    IntegerProperty sparkDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(sparkDistance, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(sparkDistance, maxDistance)));
    addTimeline(timeline);

    sparkDistance.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object distance, Object newVal) {
            renderSparkFrame((Integer) distance);
          }
        });
  }

  private int getMaxDistanceToEdge() {
    ViewExtent extent = getViewport().getExtent();
    return IntStream.of(
            centerX - extent.getXLow(),
            extent.getXHigh() - centerX,
            centerY - extent.getYLow(),
            extent.getYHigh() - centerY)
        .max()
        .getAsInt();
  }

  // Spark runs in all four directions from the centerpoint.
  // Distance is how far the spark is from the center (in each direction).
  // Color is the color of the spark itself.
  // Spark is rendered with a tail of faded versions of the color, of length LENGTH.
  // Rendering draws black after itself to leave black behind.
  private void renderSparkFrame(int distance) {
    Viewport viewport = getViewport();
    ViewExtent extent = viewport.getExtent();
    // Render spark in diminishing strength at distance from center.
    // pos is distance from spark point
    for (int pos = 0; pos <= TAIL_LENGTH; ++pos) {
      Color moderatedColor = moderateColor(TAIL_LENGTH - pos, TAIL_LENGTH, color);
      {
        int x = centerX + distance - pos;
        if (x >= centerX && x <= extent.getXHigh()) {
          viewport.setLight(x, centerY, moderatedColor);
        }
      }
      {
        int x = centerX - (distance - pos);
        if (x <= centerX && x >= extent.getXLow()) {
          viewport.setLight(x, centerY, moderatedColor);
        }
      }
      {
        int y = centerY + distance - pos;
        if (y >= centerY && y <= extent.getYHigh()) {
          viewport.setLight(centerX, y, moderatedColor);
        }
      }
      {
        int y = centerY - (distance - pos);
        if (y <= centerY && y >= extent.getYLow()) {
          viewport.setLight(centerX, y, moderatedColor);
        }
      }
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
