package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;

import javafx.animation.Animation;
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

public class Spark extends JavafxLaunchpadApplication {

  private Viewport viewport;

  @Override
  public void run() {
    viewport = getLaunchpadClient().getViewport();
    viewport.addListener(new ViewportListener() {
      @Override
      public void onButtonPressed(int x, int y) {
        fireSpark(x, y, getBaseColor(x+y));
      }
      @Override
      public void onButtonReleased(int x, int y) {
      }
    });
  }

  private void fireSpark(int x, int y, Color color) {
    IntegerProperty sparkDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.ZERO, new KeyValue(sparkDistance, 0)),
        new KeyFrame(Duration.seconds(1), new KeyValue(sparkDistance, 20)));
    timeline.play();

    sparkDistance.addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue o, Object distance, Object newVal) {
        renderSparkFrame(x, y, (Integer)distance, color);
      }
    });
  }

  // Spark runs in all four directions from the centerpoint.
  // Distance is how far the spark is from the center (in each direction).
  // Color is the color of the spark itself.
  // Spark is rendered with a tail of faded versions of the color, of length LENGTH.
  // Rendering draws black after itself to leave black behind.
  public void renderSparkFrame(int centerX, int centerY, int distance, Color color) {
    ViewExtent extent = viewport.getExtent();
    // Render spark in diminishing strength at distance from center.
    final int LENGTH = 5;
    // pos is distance from spark point
    for (int pos = 0; pos <= LENGTH; ++pos) {
      Color moderatedColor = moderateColor(LENGTH - pos, color);
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

  private Color moderateColor(int amount, Color color) {
    double percent = (double)amount / 5;
    if (percent > 1.0) {
      percent = 0.0;
    }
    if (percent < 0.0) {
      percent = 0.0;
    }
    return Color.of(
        (int)(color.getRed() * percent),
        (int)(color.getGreen() * percent),
        (int)(color.getBlue() * percent));
  }

  // Returns a color for the given index.
  private Color getBaseColor(int index) {
    final Color colors[] = {
      Color.RED,
      Color.ORANGE,
      Color.YELLOW,
      Color.GREEN,
      Color.BLUE,
      Color.PURPLE,
    };
    return colors[index % colors.length];
  }
}
