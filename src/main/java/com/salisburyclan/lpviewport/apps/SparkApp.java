package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Spark2;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.layer.DecayingLayer;

public class SparkApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    DecayingLayer decayLayer = new DecayingLayer(viewport);
    viewport.addListener(
        new ViewportListener() {
          @Override
          public void onButtonPressed(Point p) {
            new Spark2(decayLayer.newInputBuffer(), p, getBaseColor(p)).play();
          }

          @Override
          public void onButtonReleased(Point p) {}
        });
  }

  /*
    private void setupViewport(Viewport viewport) {
      viewport.addListener(
          new ViewportListener() {
            @Override
            public void onButtonPressed(Point p) {
              new Spark(viewport, p, getBaseColor(p)).play();
            }

            @Override
            public void onButtonReleased(Point p) {}
          });
    }
  */

  // Returns a color for the given index.
  private Color getBaseColor(Point p) {
    final Color colors[] = {
      Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE,
    };
    int index = p.x() + p.y();
    index = index % colors.length;
    if (index < 0) index += colors.length;
    return colors[index];
  }
}
