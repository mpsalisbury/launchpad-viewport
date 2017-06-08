package com.salisburyclan.lpviewport.geom;

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

  public Range1 getRange(Range2 extent) {
    switch (this) {
      case LEFT:
      case RIGHT:
        return extent.yRange();
      case TOP:
      case BOTTOM:
        return extent.xRange();
      default:
        throw new IllegalArgumentException("Can't getRange with INVALID edge");
    }
  }

  public Point getPoint(Range2 extent, int location) {
    switch (this) {
      case LEFT:
        return Point.create(extent.xRange().low(), location);
      case RIGHT:
        return Point.create(extent.xRange().high(), location);
      case TOP:
        return Point.create(location, extent.yRange().high());
      case BOTTOM:
        return Point.create(location, extent.yRange().low());
      default:
        throw new IllegalArgumentException("Can't getPoint with INVALID edge");
    }
  }

  public boolean isEdge(Range2 extent, Point p) {
    switch (this) {
      case LEFT:
        return p.x() == extent.xRange().low();
      case RIGHT:
        return p.x() == extent.xRange().high();
      case TOP:
        return p.y() == extent.yRange().high();
      case BOTTOM:
        return p.y() == extent.yRange().low();
      default:
        return false;
    }
  }

  public static Edge getEdge(Range2 extent, Point p) {
    if (p.x() > extent.xRange().low() && p.x() < extent.xRange().high()) {
      if (p.y() == extent.yRange().low()) {
        return Edge.BOTTOM;
      } else if (p.y() == extent.yRange().high()) {
        return Edge.TOP;
      } else {
        return Edge.INVALID;
      }
    }
    if (p.y() > extent.yRange().low() && p.y() < extent.yRange().high()) {
      if (p.x() == extent.xRange().low()) {
        return Edge.LEFT;
      } else if (p.x() == extent.xRange().high()) {
        return Edge.RIGHT;
      } else {
        return Edge.INVALID;
      }
    }
    return Edge.INVALID;
  }
}
