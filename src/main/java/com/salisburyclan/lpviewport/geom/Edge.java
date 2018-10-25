package com.salisburyclan.lpviewport.geom;

// Describes a cartesian edge of a rectangular shape.
public enum Edge {
  LEFT,
  RIGHT,
  TOP,
  BOTTOM,
  INVALID;

  // Returns the edge of the opposite side as this edge.
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

  // Returns the 1-dimensional range of pixel locations for this edge in the given extent.
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

  // Returns the point on this edge of the given extent at the given location along the edge.
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

  // Returns whether the given point is on this edge of the given extent.
  public boolean isEdge(Range2 extent, Point p) {
    switch (this) {
      case LEFT:
        return extent.xRange().low() == p.x() && extent.yRange().isPointWithin(p.y());
      case RIGHT:
        return extent.xRange().high() == p.x() && extent.yRange().isPointWithin(p.y());
      case TOP:
        return extent.yRange().high() == p.y() && extent.xRange().isPointWithin(p.x());
      case BOTTOM:
        return extent.yRange().low() == p.y() && extent.xRange().isPointWithin(p.x());
      default:
        return false;
    }
  }

  // Returns the edge of the given extent that the given point sits on.
  // If the point is not on an edge, returns Edge.INVALID.
  // Corners are not considered on an edge because they are ambiguous (two edges).
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
