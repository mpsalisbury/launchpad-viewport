package com.salisburyclan.lpviewport.layout.linked;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Vector;

// Represents a link between two viewports.
// (fromX,fromY) must be along edge of fromViewport.
// (toX,toY) must be along opposite edge of toViewport.
public class Link {
  public Viewport fromViewport;
  public Edge fromEdge;
  // fromLink, in fromViewport space
  public Point fromPoint;
  public Viewport toViewport;
  // toLink, in toViewport space
  public Point toPoint;

  public Link(Viewport fromViewport, Edge fromEdge, Point fromPoint) {
    this.fromViewport = fromViewport;
    this.fromEdge = fromEdge;
    this.fromPoint = fromPoint;
  }

  public String toString() {
    return String.format(
        "Link(%s, %s, %s, %s, %s)", fromViewport, fromEdge, fromPoint, toViewport, toPoint);
  }

  // Returns the offset between the origin of fromViewport and the origin of toViewport
  // in absolute space.
  public Vector getOriginOffset() {
    Vector normalTo = toPoint.subtract(toViewport.getExtent().origin());
    return fromLinkTarget().subtract(normalTo);
  }

  // Returns the point of the button in the toViewport that should connect to
  // the fromLinked button, in normalized fromViewport space.
  private Vector fromLinkTarget() {
    Vector normalFrom = fromPoint.subtract(fromViewport.getExtent().origin());
    switch (fromEdge) {
      case LEFT:
        return normalFrom.add(Vector.create(-1, 0));
      case RIGHT:
        return normalFrom.add(Vector.create(1, 0));
      case TOP:
        return normalFrom.add(Vector.create(0, 1));
      case BOTTOM:
        return normalFrom.add(Vector.create(0, -1));
      default:
        throw new IllegalStateException("Invalid edge type");
    }
  }
}
