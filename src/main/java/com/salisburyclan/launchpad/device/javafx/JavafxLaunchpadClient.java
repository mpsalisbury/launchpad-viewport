package com.salisburyclan.launchpad.device.javafx;

import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.Viewport;

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
 
public class JavafxLaunchpadClient implements LaunchpadClient {

  public static final String TYPE = "javafx";
  private static final int DEFAULT_SIZE = 8;
  private Viewport viewport;

  public JavafxLaunchpadClient() {
    Stage stage = newWindow(DEFAULT_SIZE, DEFAULT_SIZE);
  }

  public JavafxLaunchpadClient(int xSize, int ySize) {
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
