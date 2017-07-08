package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;

public class Circle {

  public static void drawSinglePixelCircle(
      WriteLayer outputLayer, Point center, int radius, Color color) {
    int x = 0;
    int y = radius;
    int e = 0;
    while (y >= x) {
      drawEight(outputLayer, center, x, y, color);
      e += 2 * x + 1;
      if (2 * e + (1 - 2 * y) <= 0) {
        x++;
      } else {
        e += (1 - 2 * y);
        x++;
        y--;
      }
    }
  }

  public static void drawRangeCircle(
      WriteLayer outputLayer, Point center, int radiusIn, int radiusOut, Color color) {
    int x = 0;
    int yIn = radiusIn;
    int yOut = radiusOut - 1;
    int eIn = 0;
    int eOut = 1 - (2 * radiusOut);
    while (yIn >= x) {
      drawRange(outputLayer, center, x, yIn, yOut, color);
      //Compute new yOut and eOut
      //Try horizontal first
      eOut += 2 * x + 1;
      if (eOut >= 0) {
        //Diagonal case
        eOut -= 2 * yOut - 1;
        yOut--;
      }
      //Compute new yIn and eIn
      //Try diagonal first
      eIn += 2 * (x - yIn + 1);
      if (eIn < 0) {
        //Horizontal case
        eIn += 2 * yIn - 1;
      } else {
        //Diagonal case
        yIn--;
      }
      x++;
    }
    //After we reach the 45 degree line, we continue diagonally from the inner one to meet the outer one
    while (yIn <= yOut) {
      drawRange(outputLayer, center, x, yIn, yOut, color);
      //Compute new yOut and eOut
      //Try horizontal first
      eOut += 2 * x + 1;
      if (eOut >= 0) {
        //Diagonal case
        eOut -= 2 * yOut - 1;
        yOut--;
      }
      yIn++;
      x++;
    }
  }

  private static void drawRange(
      WriteLayer outputLayer, Point center, int x, int yIn, int yOut, Color color) {
    for (int y = yIn; y <= yOut; y++) {
      drawEight(outputLayer, center, x, y, color);
    }
  }

  private static void drawEight(WriteLayer outputLayer, Point center, int x, int y, Color color) {
    outputLayer.setPixel(center.x() + x, center.y() + y, color);
    outputLayer.setPixel(center.x() + y, center.y() + x, color);
    outputLayer.setPixel(center.x() + y, center.y() - x, color);
    outputLayer.setPixel(center.x() + x, center.y() - y, color);
    outputLayer.setPixel(center.x() - x, center.y() - y, color);
    outputLayer.setPixel(center.x() - y, center.y() - x, color);
    outputLayer.setPixel(center.x() - y, center.y() + x, color);
    outputLayer.setPixel(center.x() - x, center.y() + y, color);
  }
}
