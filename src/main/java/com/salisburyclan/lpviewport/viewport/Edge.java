package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.ViewExtent;

public enum Edge {
  LEFT,
  RIGHT,
  TOP,
  BOTTOM,
  INVALID;

  public Edge getOpposite() {
    switch (this) {
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      case TOP:
        return BOTTOM;
      case BOTTOM:
        return TOP;
      default:
        return INVALID;
    }
  }

  public Range getRange(ViewExtent extent) {
    switch (this) {
      case LEFT:
      case RIGHT:
        return new Range(extent.getYLow(), extent.getYHigh());
      case TOP:
      case BOTTOM:
        return new Range(extent.getXLow(), extent.getXHigh());
      default:
        throw new IllegalArgumentException("Can't getRange with INVALID edge");
    }
  }

  public Point getPoint(ViewExtent extent, int location) {
    switch (this) {
      case LEFT:
        return new Point(extent.getXLow(), location);
      case RIGHT:
        return new Point(extent.getXHigh(), location);
      case TOP:
        return new Point(location, extent.getYHigh());
      case BOTTOM:
        return new Point(location, extent.getYLow());
      default:
        throw new IllegalArgumentException("Can't getPoint with INVALID edge");
    }
  }

  public boolean isEdge(ViewExtent extent, int x, int y) {
    switch (this) {
      case LEFT:
        return x == extent.getXLow();
      case RIGHT:
        return x == extent.getXHigh();
      case TOP:
        return y == extent.getYHigh();
      case BOTTOM:
        return y == extent.getYLow();
      default:
        return false;
    }
  }

  public static Edge getEdge(ViewExtent extent, int x, int y) {
    if (x > extent.getXLow() && x < extent.getXHigh()) {
      if (y == extent.getYLow()) {
        return Edge.BOTTOM;
      } else if (y == extent.getYHigh()) {
        return Edge.TOP;
      } else {
        return Edge.INVALID;
      }
    }
    if (y > extent.getYLow() && y < extent.getYHigh()) {
      if (x == extent.getXLow()) {
        return Edge.LEFT;
      } else if (x == extent.getXHigh()) {
        return Edge.RIGHT;
      } else {
        return Edge.INVALID;
      }
    }
    return Edge.INVALID;
  }
}
