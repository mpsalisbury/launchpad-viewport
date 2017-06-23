package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.WriteLayer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

// TODO Turn into an animation.
public class DiagRotate extends JavafxLaunchpadApplication {

  private static final DColor BOX_COLOR = DColor.BLUE;
  private static final DColor BAR_COLOR = DColor.ORANGE;
  private WriteLayer outputLayer;
  private Range2 barExtent;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    this.outputLayer = viewport.addLayer();
    barExtent = viewport.getExtent().inset(1, 1, 1, 1);
    rotate();
  }

  private void rotate() {
    IntegerProperty barLocation = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline
        .getKeyFrames()
        .addAll(
            //        new KeyFrame(Duration.ZERO, new KeyValue(barLocation, 0)),
            new KeyFrame(Duration.seconds(1), new KeyValue(barLocation, 3)));
    timeline.play();

    renderBox();
    barLocation.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object oldLocation, Object newLocation) {
            renderBars((Integer) newLocation, BAR_COLOR);
            renderBars((Integer) oldLocation, DColor.BLACK);
          }
        });
  }

  private void renderBox() {
    Range2 extent = outputLayer.getExtent();
    extent
        .xRange()
        .forEach(
            x -> {
              outputLayer.setPixel(x, extent.yRange().low(), BOX_COLOR);
              outputLayer.setPixel(x, extent.yRange().high(), BOX_COLOR);
            });
    extent
        .yRange()
        .forEach(
            y -> {
              outputLayer.setPixel(extent.xRange().low(), y, BOX_COLOR);
              outputLayer.setPixel(extent.xRange().high(), y, BOX_COLOR);
            });
  }

  private void renderBars(int linePosition, DColor color) {
    for (int x = linePosition; x < barExtent.getWidth() + barExtent.getHeight(); x += 4) {
      renderBar(x, color);
    }
  }

  private void renderBar(int linePosition, DColor color) {
    int x1 = barExtent.xRange().low();
    int y1 = barExtent.yRange().low() + linePosition;
    int x2 = barExtent.xRange().low() + linePosition;
    int y2 = barExtent.yRange().low();
    if (y1 > barExtent.yRange().high()) {
      int overshoot = y1 - barExtent.yRange().high();
      y1 -= overshoot;
      x1 += overshoot;
    }
    if (x2 > barExtent.xRange().high()) {
      int overshoot = x2 - barExtent.xRange().high();
      x2 -= overshoot;
      y2 += overshoot;
    }
    for (int x = x1, y = y1; x <= x2; ++x, --y) {
      outputLayer.setPixel(x, y, color);
    }
  }
}
