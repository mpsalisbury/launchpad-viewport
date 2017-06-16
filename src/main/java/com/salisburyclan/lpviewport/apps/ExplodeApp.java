package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.Explode;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;

public class ExplodeApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getRawViewport(this::setupViewport);
  }

  private void setupViewport(RawViewport viewport) {
    viewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            new Explode(viewport, p, getBaseColor(p)).play();
          }

          @Override
          public void onButtonReleased(Point p) {}
        });
  }

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
