package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.LayerBuffer;
import com.salisburyclan.lpviewport.api.LayerScaler;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Range2Scaler;

public class SmoothCircleApp extends LaunchpadApplication {

  private WriteLayer bufferLayer;
  private Range2Scaler scaler;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    Range2 outputExtent = viewport.getExtent();
    Range2 sourceExtent = Range2.create(outputExtent.size().scale(3));
    LayerBuffer sourceLayer = new LayerBuffer(sourceExtent);
    this.bufferLayer = sourceLayer;
    LayerScaler layerScaler = new LayerScaler(sourceLayer, outputExtent);
    this.scaler = layerScaler.getScaler().invert();
    viewport.addLayer(layerScaler);
    viewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            drawCircle(p, Color.YELLOW);
          }
        });
  }

  private void drawCircle(Point center, Color color) {
    Point bufferCenter = scaler.mapToPoint(center);
    Circle.drawRangeCircle(bufferLayer, bufferCenter, 4, 8, color);
  }
}
