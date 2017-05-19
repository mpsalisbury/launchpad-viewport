package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.Viewport;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
 
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