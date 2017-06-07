package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.geom.Point;

public interface ButtonGridListener {
  void onButtonPressed(Point p);

  void onButtonReleased(Point p);
}
