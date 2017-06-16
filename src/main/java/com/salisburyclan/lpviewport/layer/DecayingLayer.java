package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Range2;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// Copies the screenbuffer into the viewport, decaying the
// existing viewport with each time tick.
public class DecayingLayer implements Layer {
  // Underlying buffer to write to.
  //  private LayerBuffer outputBuffer;
  private Range2 extent;
  // Buffers that clients draw in.
  private LayerSandwich inputLayers;
  // Buffer that represents the decay from the input buffer.
  private LayerBuffer decayBuffer;

  // How frequently to update the buffer and output pixels.
  private static final int TICKS_PER_SECOND = 30;
  // How long for a pixel to decay to fully transparent.
  private static final int MILLIS_TO_DECAY = 500;
  // How much to decay the buffer for each tick.
  private double decayPerTick;

  public DecayingLayer(Range2 extent) {
    this.extent = extent;
    this.inputLayers = new LayerSandwich(extent);
    this.decayBuffer = new LayerBuffer(extent);
    setupDecay(TICKS_PER_SECOND, MILLIS_TO_DECAY);
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public Pixel getPixel(int x, int y) {
    return Pixel.EMPTY.combine(decayBuffer.getPixel(x, y)).combine(inputLayers.getPixel(x, y));
  }

  @Override
  public void addPixelListener(PixelListener listener) {
    decayBuffer.addPixelListener(listener);
    inputLayers.addPixelListener(listener);
  }

  @Override
  public void addCloseListener(CloseListener listener) {
    inputLayers.addCloseListener(listener);
  }

  public DecayingBuffer newInputBuffer() {
    DecayingBuffer buffer = new DecayingBuffer(this, extent);
    inputLayers.addLayer(buffer);
    return buffer;
  }

  public void removeInputBuffer(DecayingBuffer buffer) {
    inputLayers.removeLayer(buffer);
  }

  public void stopOnFinished() {
    //TODO    this.stopOnFinished = true; // ???
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
  }

  private void decayCycle() {
    decayBuffer();
    //    writeOutput();
  }

  private void decayBuffer() {
    decayBuffer
        .getExtent()
        .forEach(
            (x, y) -> {
              Pixel pixel = decayBuffer.getPixel(x, y);
              if (!pixel.equals(Pixel.EMPTY)) {
                decayBuffer.setPixel(x, y, decayPixel(pixel));
              }
            });
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
  public void pushFrame(LayerBuffer frame) {
    // TODO check frame extent
    decayBuffer
        .getExtent()
        .forEach(
            (x, y) -> {
              Pixel inputPixel = frame.getPixel(x, y);
              if (!inputPixel.equals(Pixel.EMPTY)) {
                decayBuffer.combinePixel(x, y, inputPixel);
                frame.setPixel(x, y, Pixel.EMPTY);
              }
            });
  }

  // Write input + decay into viewport.
  /*
  private void writeOutput() {
    decayBuffer
        .getExtent()
        .forEach(
            (x, y) -> {
              Pixel pixel = Pixel.BLACK.combine(decayBuffer.getPixel(x, y));
              pixel = pixel.combine(inputLayers.getPixel(x, y));
              outputBuffer.setPixel(x, y, pixel);
            });
  }
  */
}
