package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.DecayingAnimation;
import com.salisburyclan.lpviewport.animation.Spark;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;

public class SparkApp extends JavafxLaunchpadApplication {

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

          @Override
          public void onButtonReleased(Point p) {}
        });
  }

  // Returns a color for the given index.
  private DColor getBaseColor(Point p) {
    final DColor colors[] = {
      DColor.RED, DColor.ORANGE, DColor.YELLOW, DColor.GREEN, DColor.BLUE, DColor.PURPLE,
    };
    int index = p.x() + p.y();
    index = index % colors.length;
    if (index < 0) index += colors.length;
    return colors[index];
  }
}
