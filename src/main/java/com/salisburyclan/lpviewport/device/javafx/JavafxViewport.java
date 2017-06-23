package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.viewport.Button2ListenerMultiplexer;

public class JavafxViewport implements RawViewport {
  ColorButtonGrid buttonGrid;
  Button2ListenerMultiplexer listenerMultiplexer;
  Range2 extent;
  LightLayer outputLayer;

  public JavafxViewport(ColorButtonGrid buttonGrid) {
    this.buttonGrid = buttonGrid;
    outputLayer = new ButtonGridLightLayer();
    extent = Range2.create(0, 0, buttonGrid.getWidth() - 1, buttonGrid.getHeight() - 1);

    listenerMultiplexer = new Button2ListenerMultiplexer();
    buttonGrid.addListener(
        new ButtonGridListener() {
          @Override
          public void onButtonPressed(Point p) {
            listenerMultiplexer.onButtonPressed(p);
          }

          @Override
          public void onButtonReleased(Point p) {
            listenerMultiplexer.onButtonReleased(p);
          }
        });
  }

  @Override
  public LightLayer getLightLayer() {
    return outputLayer;
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void addListener(Button2Listener listener) {
    listenerMultiplexer.add(listener);
  }

  @Override
  public void removeListener(Button2Listener listener) {
    listenerMultiplexer.remove(listener);
  }

  private class ButtonGridLightLayer implements LightLayer {
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
      extent.forEach(
          (x, y) -> {
            buttonGrid.setButtonColor(x, y, javafxColor);
          });
    }

    private javafx.scene.paint.Color launchpadColorToJavafxColor(Color color) {
      int red = color.getRed() * 255 / Color.MAX_INTENSITY;
      int green = color.getGreen() * 255 / Color.MAX_INTENSITY;
      int blue = color.getBlue() * 255 / Color.MAX_INTENSITY;
      return javafx.scene.paint.Color.rgb(red, green, blue);
    }
  }
}
