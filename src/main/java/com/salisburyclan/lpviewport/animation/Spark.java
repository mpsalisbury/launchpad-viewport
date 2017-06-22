package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.layer.FrameWriteLayer;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class Spark extends FramedAnimation {

  private final Point center;
  private final DColor color;
  private FrameWriteLayer layer;

  public static void play(RawViewport rawViewport, Point center, Color color) {
    Spark spark = new Spark(rawViewport.getExtent(), center, color);
    Viewport viewport = new Viewport(rawViewport);
    viewport.addLayer(new DecayingAnimation(spark));
    spark.play();
  }

  public Spark(Range2 extent, Point center, Color color) {
    super(extent);
    this.layer = getWriteLayer();
    this.center = center;
    this.color = DColor.create(color);
    init();
  }

  protected void init() {
    final int maxDistance = getMaxDistanceToEdge() + 1;
    IntegerProperty sparkDistance = new SimpleIntegerProperty();
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(Duration.ZERO, new KeyValue(sparkDistance, 0)),
            new KeyFrame(Duration.millis(500), new KeyValue(sparkDistance, maxDistance)));
    timeline.setOnFinished(
        event -> {
          layer.close();
        });
    addTimeline(timeline);

    sparkDistance.addListener(
        new ChangeListener() {
          @Override
          public void changed(ObservableValue o, Object distance, Object newVal) {
            renderSparkFrame((Integer) distance);
          }
        });
  }

  private int getMaxDistanceToEdge() {
    Range2 extent = layer.getExtent();
    return IntStream.of(
            center.x() - extent.xRange().low() + 1,
            extent.xRange().high() - center.x() + 1,
            center.y() - extent.yRange().low() + 1,
            extent.yRange().high() - center.y() + 1)
        .max()
        .getAsInt();
  }

  // Spark runs in all four directions from the centerpoint.
  // Distance is how far the spark is from the center (in each direction).
  // Color is the color of the spark itself.
  // Rendering does not draw black after itself to erase.
  // Should be used with decaying layer.
  private void renderSparkFrame(int distance) {
    layer.nextFrame();
    Range2 extent = layer.getExtent();
    // Render spark at distance from center.
    layer.setPixel(center.x() + distance, center.y(), color);
    layer.setPixel(center.x() - distance, center.y(), color);
    layer.setPixel(center.x(), center.y() + distance, color);
    layer.setPixel(center.x(), center.y() - distance, color);
  }
}
