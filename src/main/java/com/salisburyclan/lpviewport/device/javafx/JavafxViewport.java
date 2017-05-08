package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.device.ListenerMultiplexer;
import com.salisburyclan.lpviewport.device.SubViewport;
import com.salisburyclan.lpviewport.device.SubViewStrip;

public class JavafxViewport implements Viewport {
  ColorButtonGrid buttonGrid;
  ListenerMultiplexer listenerMultiplexer;
  ViewExtent extent;

  public JavafxViewport(ColorButtonGrid buttonGrid) {
    this.buttonGrid = buttonGrid;
    listenerMultiplexer = new ListenerMultiplexer();

    buttonGrid.addListener(new ButtonGridListener() {
      @Override
      public void onButtonPressed(int x, int y) {
        listenerMultiplexer.onButtonPressed(x, y);
      }
      @Override
      public void onButtonReleased(int x, int y) {
        listenerMultiplexer.onButtonReleased(x, y);
      }
    });

    extent = new ViewExtent(0, 0, buttonGrid.getWidth() - 1, buttonGrid.getHeight() - 1);
  }

  @Override
  public ViewExtent getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    buttonGrid.setButtonColor(x, y, launchpadColorToJavafxColor(color));
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
  public Viewport getSubViewport(ViewExtent extent) {
    return new SubViewport(this, extent);
  }

  @Override
  public ViewStrip getSubViewStrip(ViewExtent extent) {
    return new SubViewStrip(this, extent);
  }

  @Override
  public ViewButton getSubViewButton(int x, int y) {
    return new SubViewport.SubViewButton(this, x, y);
  }
}
