package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.device.ViewportAdapter;

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
    viewport = new ViewportAdapter(new JavafxViewport(buttonGrid));

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
