package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Spark;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.DecayingAnimation;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

public class SparkApp extends LaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    Range2 extent = viewport.getExtent();
    viewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            Spark spark = new Spark(extent, p, getBaseColor(p));
            viewport.addLayer(new DecayingAnimation(spark));
            spark.play();
          }
        });
  }

  // Returns a color for the given index.
  private Color getBaseColor(Point p) {
    final Color colors[] = {
      Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE, Color.BROWN,
    };
    int index = p.x() + p.y();
    index = index % colors.length;
    if (index < 0) index += colors.length;
    return colors[index];
  }
}
