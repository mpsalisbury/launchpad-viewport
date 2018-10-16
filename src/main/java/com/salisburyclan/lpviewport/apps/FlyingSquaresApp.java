package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.MoveSprite;
import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.DecayingAnimation;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.util.Duration;

public class FlyingSquaresApp extends LaunchpadApplication {

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    animateSprite(viewport, makeSquare(4, Color.RED), Duration.seconds(1));
    animateSprite(viewport, makeSquare(4, Color.YELLOW), Duration.seconds(2));
    animateSprite(viewport, makeSquare(4, Color.BLUE), Duration.seconds(3));
  }

  private void animateSprite(Viewport viewport, ReadLayer sprite, Duration cycleTime) {
    AnimatedLayer animatedSprite = new MoveSprite(viewport.getExtent(), sprite, cycleTime);
    viewport.addLayer(new DecayingAnimation(animatedSprite));
    animatedSprite.play();
  }

  private ReadLayer makeSquare(int size, Color color) {
    Pixel pixel = Pixel.create(color);
    Range2 extent = Range2.create(0, 0, size - 1, size - 1);
    LayerBuffer square = new LayerBuffer(extent);
    extent
        .xRange()
        .forEach(
            x -> {
              square.setPixel(x, extent.yRange().low(), pixel);
              square.setPixel(x, extent.yRange().high(), pixel);
            });
    extent
        .yRange()
        .forEach(
            y -> {
              square.setPixel(extent.xRange().low(), y, pixel);
              square.setPixel(extent.xRange().high(), y, pixel);
            });
    return square;
  }
}
