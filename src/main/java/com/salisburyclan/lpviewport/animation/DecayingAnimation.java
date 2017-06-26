package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.CloseListener;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.PixelListener;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CleanupExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// Copies the inputLayer into the decayingLayer, decaying it
// with each time tick.
// TODO make this an AnimatedLayer
public class DecayingAnimation implements ReadLayer {
  private Range2 extent;

  // The layer into which the source layer draws.
  private ReadLayer inputLayer;
  // The decay from the input layer.
  private LayerBuffer decayLayer;

  // Indicator that inputLayer has closed and that
  // we should close when decay has completed.
  private boolean shuttingDown = false;

  private CleanupExecutor onCleanup;

  // How frequently to update the buffer and output pixels.
  private static final int TICKS_PER_SECOND = 30;
  // How long for a pixel to decay to fully transparent.
  private static final int MILLIS_TO_DECAY = 500;
  // How much to decay the buffer for each tick.
  private double decayPerTick;

  public DecayingAnimation(ReadLayer inputLayer) {
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
          public void onSetPixel(int x, int y) {}
        });

    setupDecay(TICKS_PER_SECOND, MILLIS_TO_DECAY);
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
    onCleanup.add(
        () -> {
          decayLayer.removePixelListener(listener);
          inputLayer.removePixelListener(listener);
        });
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    decayLayer.removePixelListener(listener);
    inputLayer.removePixelListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    // TODO only close when we're done after this.
    inputLayer.addCloseListener(
        new CloseListener() {
          @Override
          public void onClose() {
            shuttingDown = true;
          }
        });
  }

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

  private Pixel decayPixel(Pixel pixel) {
    double newAlpha = Math.max(0.0, pixel.alpha() - decayPerTick);
    if (newAlpha > 0.0) {
      return Pixel.create(pixel.color(), newAlpha);
    } else {
      return Pixel.EMPTY;
    }
  }

  // Push input frame into decaying frame and clear input frame.
  public void pushFrame() {
    // TODO check frame extent
    extent.forEach(
        (x, y) -> {
          Pixel inputPixel = inputLayer.getPixel(x, y);
          if (!inputPixel.equals(Pixel.EMPTY)) {
            decayLayer.combinePixel(x, y, inputPixel);
          }
        });
  }
}
