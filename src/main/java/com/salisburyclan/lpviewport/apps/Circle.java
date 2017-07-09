package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;

// Utilities to draw circles into WriteLayers.
public class Circle {

  // Draws single-pixel-thick circle whose pixel centers are closest
  // to the actual circle.
  // Strategy, draw 1/8th of circle (starting at top, to right).
  // Each x value will draw a pixel at one y value.
  // For each step of x, determine whether y stays same or goes down one.
  // To decide, evaluate which of those y's is closer to the actual circle.
  // Do this by evaluating x^2 + y^2 - r^2 as the error.  Smaller absolute
  // error means closer to circle. We compute this error incrementally with
  // each step in x.
  public static void drawSinglePixelCircle(
      WriteLayer outputLayer, Point center, int radius, Color color) {
    int x = 0;
    int y = radius;
    int e = 0;
    while (y >= x) {
      drawEight(outputLayer, center, x, y, color);
      e += 2 * x + 1;
      if (2 * e + (1 - 2 * y) > 0) {
        e += (1 - 2 * y);
        y--;
      }
      x++;
    }
  }

  // Draws a thick circle whose pixel centers are between the given radii.
  // Strategy is similar to drawSinglePixelCircle. Instead of tracking one
  // circle around the top 1/8th, we track two, an inner and outer circle.
  // We track the error term for both circles and ensure that the top pixel
  // is inside of the outer circle, and the bottom pixel is no lower than the
  // inner circle. This lets us draw all pixels whose centers are between the
  // two circles, including those exactly on the inner circle and excluding
  // those exactly on the outer circle. So a contiguous set of circular bands
  // is guaranteed to cover every pixel exactly once.
  public static void drawRangeCircle(
      WriteLayer outputLayer, Point center, int radiusIn, int radiusOut, Color color) {
    int x = 0;
    int yIn = radiusIn;
    int yOut = radiusOut - 1;
    int eIn = 0;
    int eOut = 1 - (2 * radiusOut);
    while (yIn >= x) {
      drawRange(outputLayer, center, x, yIn, yOut, color);
      // Compute new yOut and eOut.
      // Move right as long as our center is still inside the outer circle.
      // Try horizontal
      eOut += 2 * x + 1;
      if (eOut >= 0) {
        // Diagonal case
        eOut -= 2 * yOut - 1;
        yOut--;
      }
      // Compute new yIn and eIn
      // Move diagonal (down/right) as long as our center is still outside the inner circle.
      // Try diagonal
      eIn += 2 * (x - yIn + 1);
      yIn--;
      if (eIn < 0) {
        // Horizontal case
        eIn += 2 * yIn - 1;
        yIn++;
      }
      x++;
    }
    // After we reach the 45 degree line, we continue diagonally from the inner one to meet the outer one
    while (yIn <= yOut) {
      drawRange(outputLayer, center, x, yIn, yOut, color);
      // Compute new yOut and eOut
      // Try horizontal
      eOut += 2 * x + 1;
      if (eOut >= 0) {
        // Diagonal case
        eOut -= 2 * yOut - 1;
        yOut--;
      }
      yIn++;
      x++;
    }
  }

  // Draws a vertical line of pixels for the top eighth circle band,
  // and copy that for the other seven eighths.
  private static void drawRange(
      WriteLayer outputLayer, Point center, int x, int yIn, int yOut, Color color) {
    for (int y = yIn; y <= yOut; y++) {
      drawEight(outputLayer, center, x, y, color);
    }
  }

  // Draws the given pixel from the top eighth of a circle, and make
  // copies for the other seven eighths.  x and y are relative to the
  // center point.
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
