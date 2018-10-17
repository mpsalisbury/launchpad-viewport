package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.LayerTranslator;
import com.salisburyclan.lpviewport.api.ReadLayer;
import com.salisburyclan.lpviewport.geom.Range1;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// Moves the sprite around the edges of the moveExtent.
public class MoveSprite extends AnimatedLayer {
  private final LayerTranslator translator;
  private final Range2 moveExtent;
  // Number of animation steps in x direction.
  private final int xSize;
  // Number of animation steps in y direction.
  private final int ySize;

  public MoveSprite(Range2 moveExtent, ReadLayer sprite, Duration cycleTime) {
    this.translator = new LayerTranslator(sprite);
    this.moveExtent = moveExtent;
    this.xSize = moveExtent.xRange().size() - translator.getExtent().xRange().size();
    this.ySize = moveExtent.yRange().size() - translator.getExtent().yRange().size();
    init(cycleTime);
  }

  @Override
  protected ReadLayer getReadLayer() {
    return translator;
  }

  // We proxy the translator for every aspect of our ReadLayer except for its extent.
  @Override
  public Range2 getExtent() {
    return moveExtent;
  }

  protected void init(Duration cycleTime) {
    IntegerProperty layerPosition = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);

    Range1 animationRange = getAnimationRange();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(layerPosition, animationRange.low())),
            new KeyFrame(cycleTime, new KeyValue(layerPosition, animationRange.high())));
    addTimeline(timeline);

    layerPosition.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldStep, Object newStep) {
            Vector newOffset = getAnimationOffset((Integer) newStep);
            translator.setOffset(newOffset);
          }
        });
  }

  // Returns the range of animation steps to iterate through.
  private Range1 getAnimationRange() {
    return Range1.create(0, xSize * 2 + ySize * 2 - 1);
  }

  // Returns the frame offset for the given animation step.
  // Animation moves the sprite around the edges of the moveExtent.
  private Vector getAnimationOffset(int animationStep) {
    final int phase2Step = xSize;
    final int phase3Step = xSize + ySize;
    final int phase4Step = xSize + ySize + xSize;

    final Range1 xRange = moveExtent.xRange();
    final Range1 yRange = moveExtent.yRange();

    if (animationStep < phase2Step) {
      int phaseStep = animationStep;
      return Vector.create(xRange.low() + phaseStep, yRange.low());
    }
    if (animationStep < phase3Step) {
      int phaseStep = animationStep - phase2Step;
      return Vector.create(xRange.low() + xSize, yRange.low() + phaseStep);
    }
    if (animationStep < phase4Step) {
      int phaseStep = animationStep - phase3Step;
      return Vector.create(xRange.low() + xSize - phaseStep, yRange.low() + ySize);
    }
    {
      int phaseStep = animationStep - phase4Step;
      return Vector.create(xRange.low(), yRange.low() + ySize - phaseStep);
    }
  }
}
