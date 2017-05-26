package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.Viewport;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavafxDevice implements LaunchpadDevice {

  public static final String TYPE = "javafx";
  private static final int DEFAULT_SIZE = 8;
  private Viewport viewport;

  public JavafxDevice() {
    Stage stage = newWindow(DEFAULT_SIZE, DEFAULT_SIZE);
  }

  public JavafxDevice(int xSize, int ySize) {
    Stage stage = newWindow(xSize, ySize);
  }

  public Stage newWindow(int xSize, int ySize) {
    ColorButtonGrid buttonGrid = new ColorButtonGrid(xSize, ySize, 40);
    viewport = new JavafxViewport(buttonGrid);

    Stage stage = new Stage();
    stage.setScene(new Scene(buttonGrid.getParent()));
    stage.sizeToScene();
    stage.setResizable(false);
    stage.initStyle(StageStyle.UTILITY);
    //    stage.setTitle("foo");
    stage.show();
    return stage;
  }

  public String getType() {
    return TYPE;
  }

  public Viewport getViewport() {
    return viewport;
  }

  public void close() {
    // TODO
  }
}
