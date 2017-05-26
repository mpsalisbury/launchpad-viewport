package com.salisburyclan.lpviewport.device.javafx;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ColorButtonGrid {
  private Parent parent;
  private Button[][] buttons;
  private int xCount;
  private int yCount;
  private List<ButtonGridListener> listeners;

  public ColorButtonGrid(int xCount, int yCount, int size) {
    this.xCount = xCount;
    this.yCount = yCount;
    this.buttons = new Button[xCount][yCount];
    this.parent = initializeButtons(xCount, yCount, size);
    this.listeners = new ArrayList<>();
  }

  private Parent initializeButtons(int xCount, int yCount, int size) {
    VBox vBox = new VBox();
    vBox.setSpacing(5);
    vBox.setBackground(
        new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    for (int y = yCount - 1; y >= 0; y--) { // Build y largest->smallest top->bottom
      HBox hBox = new HBox();
      hBox.setSpacing(5);
      for (int x = 0; x < xCount; x++) {
        Button button = new Button();
        button.setPrefSize(size, size);
        setButtonColor(button, Color.BLACK);
        int capturedX = x;
        int capturedY = y;
        button.setOnMousePressed(
            event -> {
              listeners.forEach(
                  listener -> {
                    listener.onButtonPressed(capturedX, capturedY);
                  });
            });
        button.setOnMouseReleased(
            event -> {
              listeners.forEach(
                  listener -> {
                    listener.onButtonReleased(capturedX, capturedY);
                  });
            });
        hBox.getChildren().add(button);
        buttons[x][y] = button;
      }
      vBox.getChildren().add(hBox);
    }
    return vBox;
  }

  public Parent getParent() {
    return parent;
  }

  public int getWidth() {
    return xCount;
  }

  public int getHeight() {
    return yCount;
  }

  public Button getButton(int x, int y) {
    if (x < 0 || x >= xCount || y < 0 || y >= yCount) {
      return null;
      //throw new IllegalArgumentException(String.format("Button out of range (%s, %s)", x, y));
    }
    return buttons[x][y];
  }

  public void setButtonColor(int x, int y, Color color) {
    setButtonColor(getButton(x, y), color);
  }

  private void setButtonColor(Button button, Color color) {
    if (button != null) {
      button.setStyle(
          String.format(
              "-fx-base: rgb(%s,%s,%s);",
              color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255));
    }
  }

  public void addListener(ButtonGridListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ButtonGridListener listener) {
    // TODO implement
    throw new UnsupportedOperationException();
  }
}
