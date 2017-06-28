package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CleanupExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// Wraps a layer and decays each pixel's brightness over time.
// The input layer is copied into a decaying layer on each frame (nextFrame()).
// The decaying layer decays pixels towards transparent over time.
// TODO Consider making this an AnimatedLayer
public class DecayingAnimation implements ReadLayer {
  private Range2 extent;

  // The layer into which the source layer draws.
  private ReadLayer inputLayer;
  // The decay from the input layer.
  private LayerBuffer decayLayer;

  // Indicator that inputLayer has closed and that
  // we should close when decay has completed.
  private boolean shuttingDown = false;

  // Methods to execute at cleanup time.
  private CleanupExecutor onCleanup;

  // How much to decay the buffer transparency for each tick.
  private double decayPerTick;

  private static final int DEFAULT_TICKS_PER_SECOND = 30;
  private static final int DEFAULT_MILLIS_TO_DECAY = 500;

  public DecayingAnimation(ReadLayer inputLayer) {
    this(inputLayer, DEFAULT_TICKS_PER_SECOND, DEFAULT_MILLIS_TO_DECAY);
  }

  // @Param ticks_per_second how frequently to update the buffer and output pixels.
  // @Param millis_to_decay How long for an opaque pixel to decay to fully transparent.
  public DecayingAnimation(ReadLayer inputLayer, int ticks_per_second, int millis_to_decay) {
    this.inputLayer = inputLayer;
    this.extent = inputLayer.getExtent();
    this.decayLayer = new LayerBuffer(extent);
    this.onCleanup = new CleanupExecutor();

    inputLayer.addPixelListener(
        new PixelListener() {
          @Override
          public void onNextFrame() {
            // Copy input frame before it draws next frame.
            pushFrame();
          }

          @Override
          public void onPixelChanged(Point p) {}

          @Override
          public void onPixelsChanged(Range2 range) {}
        });

    setupDecay(ticks_per_second, millis_to_decay);
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    return decayLayer.getPixel(x, y).combine(inputLayer.getPixel(x, y));
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    decayLayer.addPixelListener(listener);
    inputLayer.addPixelListener(listener);
    onCleanup.add(() -> removePixelListener(listener));
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    decayLayer.removePixelListener(listener);
    inputLayer.removePixelListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    inputLayer.addCloseListener(
        new CloseListener() {
          @Override
          public void onClose() {
            shuttingDown = true;
          }
        });
  }

  // Set up decay animation.
  private void setupDecay(int ticksPerSecond, double millisToDecay) {
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    int millisPerTick = 1000 / ticksPerSecond;
    timeline
        .getKeyFrames()
        .add(new KeyFrame(Duration.millis(millisPerTick), event -> decayCycle()));
    this.decayPerTick = millisPerTick / millisToDecay;
    timeline.play();
    onCleanup.add(() -> timeline.stop());
  }

  // Executed at every decay tick.
  private void decayCycle() {
    AtomicBoolean foundNonEmptyPixel = new AtomicBoolean(false);
    extent.forEach(
        (x, y) -> {
          Pixel pixel = decayLayer.getPixel(x, y);
          if (!pixel.equals(Pixel.EMPTY)) {
            foundNonEmptyPixel.set(true);
            decayLayer.setPixel(x, y, decayPixel(pixel));
          }
        });
    if (!foundNonEmptyPixel.get() && shuttingDown) {
      onCleanup.execute();
    }
  }

  // Compute next decay level for given pixel.
  private Pixel decayPixel(Pixel pixel) {
    double newAlpha = Math.max(0.0, pixel.alpha() - decayPerTick);
    if (newAlpha > 0.0) {
      return Pixel.create(pixel.color(), newAlpha);
    } else {
      return Pixel.EMPTY;
    }
  }

  // Copy input frame into decaying frame.
  public void pushFrame() {
    extent.forEach(
        (x, y) -> {
          Pixel inputPixel = inputLayer.getPixel(x, y);
          if (!inputPixel.equals(Pixel.EMPTY)) {
            decayLayer.combinePixel(x, y, inputPixel);
          }
        });
  }
}
