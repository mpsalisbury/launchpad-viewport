package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.viewport.ListenerMultiplexer;

public class JavafxViewport implements Viewport {
  ColorButtonGrid buttonGrid;
  ListenerMultiplexer listenerMultiplexer;
  Range2 extent;

  public JavafxViewport(ColorButtonGrid buttonGrid) {
    this.buttonGrid = buttonGrid;
    listenerMultiplexer = new ListenerMultiplexer();

    buttonGrid.addListener(
        new ButtonGridListener() {
          @Override
          public void onButtonPressed(int x, int y) {
            listenerMultiplexer.onButtonPressed(x, y);
          }

          @Override
          public void onButtonReleased(int x, int y) {
            listenerMultiplexer.onButtonReleased(x, y);
          }
        });

    extent = Range2.create(0, 0, buttonGrid.getWidth() - 1, buttonGrid.getHeight() - 1);
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    buttonGrid.setButtonColor(x, y, launchpadColorToJavafxColor(color));
  }

  @Override
  public void setAllLights(Color color) {
    javafx.scene.paint.Color javafxColor = launchpadColorToJavafxColor(color);
    extent
        .xRange()
        .stream()
        .forEach(
            x -> {
              extent
                  .yRange()
                  .stream()
                  .forEach(
                      y -> {
                        buttonGrid.setButtonColor(x, y, javafxColor);
                      });
            });
  }

  private javafx.scene.paint.Color launchpadColorToJavafxColor(Color color) {
    int red = color.getRed() * 255 / Color.MAX_INTENSITY;
    int green = color.getGreen() * 255 / Color.MAX_INTENSITY;
    int blue = color.getBlue() * 255 / Color.MAX_INTENSITY;
    return javafx.scene.paint.Color.rgb(red, green, blue);
  }

  @Override
  public void addListener(ViewportListener listener) {
    listenerMultiplexer.addListener(listener);
  }

  @Override
  public void removeListener(ViewportListener listener) {
    listenerMultiplexer.removeListener(listener);
  }
}
