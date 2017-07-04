package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.viewport.Button2ListenerMultiplexer;
import com.salisburyclan.lpviewport.viewport.RawLayer;
import com.salisburyclan.lpviewport.viewport.RawViewport;

public class JavafxViewport implements RawViewport {
  ColorButtonGrid buttonGrid;
  Button2ListenerMultiplexer listenerMultiplexer;
  Range2 extent;
  RawLayer outputLayer;

  public JavafxViewport(ColorButtonGrid buttonGrid) {
    this.buttonGrid = buttonGrid;
    outputLayer = new ButtonGridRawLayer();
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
  public RawLayer getRawLayer() {
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

  private class ButtonGridRawLayer implements RawLayer {
    @Override
    public Range2 getExtent() {
      return extent;
    }

    @Override
    public void setPixel(int x, int y, Color color) {
      buttonGrid.setButtonColor(x, y, launchpadColorToJavafxColor(color));
    }

    @Override
    public void setAllPixels(Color color) {
      javafx.scene.paint.Color javafxColor = launchpadColorToJavafxColor(color);
      extent.forEach(
          (x, y) -> {
            buttonGrid.setButtonColor(x, y, javafxColor);
          });
    }

    private javafx.scene.paint.Color launchpadColorToJavafxColor(Color color) {
      int red = (int) (color.red() * 255.0);
      int green = (int) (color.green() * 255.0);
      int blue = (int) (color.blue() * 255.0);
      return javafx.scene.paint.Color.rgb(red, green, blue);
    }
  }
}
