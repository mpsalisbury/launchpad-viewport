package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CloseListenerMultiplexer;
import com.salisburyclan.lpviewport.util.CommandExecutor;
import com.salisburyclan.lpviewport.util.PixelListenerMultiplexer;
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

  private CloseListenerMultiplexer closeListeners;
  private PixelListenerMultiplexer pixelListeners;

  // Indicator that inputLayer has closed and that
  // we should close when decay has completed.
  private boolean shuttingDown = false;

  // Methods to execute at cleanup time.
  private CommandExecutor onCleanup;

  // How much to decay the buffer transparency for each tick.
  private double decayPerTick;

  private static final int DEFAULT_TICKS_PER_SECOND = 30;
  private static final int DEFAULT_MILLIS_TO_DECAY = 500;

  // @Param inputLayer Frame Layer that we will decay over time.
  public DecayingAnimation(ReadLayer inputLayer) {
    this(inputLayer, DEFAULT_TICKS_PER_SECOND, DEFAULT_MILLIS_TO_DECAY);
  }

  // @Param inputLayer Frame Layer that we will decay over time.
  // @Param ticksPerSecond how frequently to update the buffer and output pixels.
  // @Param millisToDecay How long for an opaque pixel to decay to fully transparent.
  public DecayingAnimation(ReadLayer inputLayer, int ticksPerSecond, int millisToDecay) {
    this(
        inputLayer,
        newTimelineTickProvider(ticksPerSecond),
        getDecayPerTick(ticksPerSecond, millisToDecay));
  }

  public interface TickProvider {
    void addTicker(Runnable listener);
  }

  // @Param inputLayer Frame Layer that we will decay over time.
  // @Param tickProvider Provides a tick for each decay update.
  // @Param decayPerTick Amount of alpha to decay each pixel on each tick.
  public DecayingAnimation(ReadLayer inputLayer, TickProvider tickProvider, double decayPerTick) {
    this.inputLayer = inputLayer;
    this.extent = inputLayer.getExtent();
    this.decayLayer = new LayerBuffer(extent);
    this.onCleanup = new CommandExecutor();
    this.closeListeners = new CloseListenerMultiplexer();
    this.pixelListeners = new PixelListenerMultiplexer();
    this.decayPerTick = decayPerTick;
    tickProvider.addTicker(this::decayCycle);

    PixelListener layerPixelListener =
        new PixelListener() {
          @Override
          public void onNextFrame() {
            // Copy input frame before it draws next frame.
            pushFrame();
          }

          @Override
          public void onPixelChanged(Point p) {
            pixelListeners.onPixelChanged(p);
          }

          @Override
          public void onPixelsChanged(Range2 range) {
            pixelListeners.onPixelsChanged(range);
          }
        };
    inputLayer.addPixelListener(layerPixelListener);
    decayLayer.addPixelListener(layerPixelListener);

    inputLayer.addCloseListener(
        new CloseListener() {
          @Override
          public void onClose() {
            pixelListeners.clear();
            shuttingDown = true;
          }
        });
  }

  private void shutDown() {
    closeListeners.onClose();
    closeListeners.clear();
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    if (shuttingDown) {
      // If shutting down, ignore inputLayer.
      return decayLayer.getPixel(x, y);
    } else {
      return decayLayer.getPixel(x, y).combine(inputLayer.getPixel(x, y));
    }
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    pixelListeners.add(listener);
  }

  @Override
  public void removePixelListener(PixelListener listener) {
    pixelListeners.remove(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    closeListeners.add(listener);
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
      shutDown();
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
  private void pushFrame() {
    if (shuttingDown) {
      return;
    }
    extent.forEach(
        (x, y) -> {
          Pixel inputPixel = inputLayer.getPixel(x, y);
          if (!inputPixel.equals(Pixel.EMPTY)) {
            decayLayer.combinePixel(x, y, inputPixel);
          }
        });
  }

  // TickProvider that fires with each call to fireTick().
  public static class SimpleTickProvider implements TickProvider {
    private CommandExecutor executor = new CommandExecutor();

    @Override
    public void addTicker(Runnable ticker) {
      executor.add(ticker);
    }

    public void fireTick() {
      executor.execute();
    }
  }

  // Returns Timeline-driven TickProvider.
  private static TickProvider newTimelineTickProvider(int ticksPerSecond) {
    SimpleTickProvider tickProvider = new SimpleTickProvider();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    int millisPerTick = 1000 / ticksPerSecond;
    timeline
        .getKeyFrames()
        .add(new KeyFrame(Duration.millis(millisPerTick), event -> tickProvider.fireTick()));
    timeline.play();
    // TODO: Figure out another way to stop the timeline.
    //    onCleanup.add(() -> timeline.stop());
    return tickProvider;
  }

  // Computes amount to decay buffer with every tick.
  private static double getDecayPerTick(int ticksPerSecond, int millisToDecay) {
    return 1000.0 / (ticksPerSecond * millisToDecay);
  }
}
