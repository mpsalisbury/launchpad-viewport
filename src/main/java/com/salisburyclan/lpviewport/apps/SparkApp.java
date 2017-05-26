package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.animation.Spark;

public class SparkApp extends JavafxLaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    viewport.addListener(new ViewportListener() {
      @Override
      public void onButtonPressed(int x, int y) {
        new Spark(viewport, x, y, getBaseColor(x+y)).play();
      }
      @Override
      public void onButtonReleased(int x, int y) {
      }
    });
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
    index = index % colors.length;
    if (index < 0) index += colors.length;
    return colors[index];
  }
}
