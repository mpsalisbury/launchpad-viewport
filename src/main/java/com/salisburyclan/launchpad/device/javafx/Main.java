package com.salisburyclan.launchpad.device.javafx;

import com.salisburyclan.launchpad.api.LaunchpadClient;

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
 
/**
 * An example of Buttons with different background colors.
 */
public class Main extends Application {
 
    public void newWindow(int x, int y) {
      LaunchpadClient client = new JavafxLaunchpadClient();
      //stage.setX(x * stage.getScene().getWindow().getWidth());
      //stage.setY(y * stage.getScene().getWindow().getHeight());
      //return stage;
    }

    @Override public void start(Stage primaryStage) throws Exception {
      newWindow(0,0);
      newWindow(1,0);
      newWindow(0,1);
      newWindow(1,1);
    }
 
    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
      launch(args);
    }
}
