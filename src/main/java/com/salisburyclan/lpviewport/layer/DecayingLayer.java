package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// Copies the screenbuffer into the viewport, decaying the
// existing viewport with each time tick.
public class DecayingLayer {
  // Underlying viewport to write to.
  private Viewport outputViewport;
  // Buffers that clients draw in.
  private List<LayerBuffer> inputBuffers;
  // Buffer that represents the decay from the input buffer.
  private LayerBuffer decayBuffer;

  public DecayingLayer(Viewport outputViewport) {
    this.outputViewport = outputViewport;
    this.inputBuffers = new ArrayList<>();
    this.decayBuffer = new LayerBuffer(outputViewport.getExtent());
    setupDecay();
  }

  public Range2 getExtent() {
    return outputViewport.getExtent();
  }

  // TODO remove input buffer when done.
  public DecayingBuffer newInputBuffer() {
    DecayingBuffer buffer = new DecayingBuffer(this, outputViewport.getExtent());
    inputBuffers.add(buffer);
    return buffer;
  }

  public void removeInputBuffer(DecayingBuffer buffer) {
    inputBuffers.remove(buffer);
  }

  private void setupDecay() {
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(30), event -> decayCycle()));
    timeline.play();
  }

  private void decayCycle() {
    decayBuffer();
    writeOutput();
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
    // TODO configure this layer with time-to-decay parameter and frames-per-second.
    double newAlpha = Math.max(0.0, pixel.alpha() - 0.05);
    return Pixel.create(pixel.color(), newAlpha);
  }

  // Push input frame into decaying output frame and clear input frame.
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

  // Write input + output into viewport.
  private void writeOutput() {
    decayBuffer
        .getExtent()
        .forEach(
            (x, y) -> {
              Pixel pixel = Pixel.BLACK.combine(decayBuffer.getPixel(x, y));
              for (LayerBuffer inputBuffer : inputBuffers) {
                pixel = pixel.combine(inputBuffer.getPixel(x, y));
              }
              outputViewport.setLight(x, y, pixel.color().color());
            });
  }
}
