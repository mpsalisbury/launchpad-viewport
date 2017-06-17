package com.salisburyclan.lpviewport.layout.linked;

import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Vector;

/**
 * Represents a link between two viewports. Used to align adjacent viewports when building a grid of
 * viewports. Viewports are aligned by marking adjacent edge points within the viewports. fromPoint
 * is a Point along an edge of fromViewport in fromViewport space. toPoint is a Point along opposite
 * edge of toViewport in toViewport space. Those points are taken to be adjacent when the viewports
 * are lined up along that common edge. getOriginOffset() will compute the offset of the origins of
 * the two viewports in order to make them adjacent in this way.
 */
public class Link {
  public RawViewport fromViewport;
  public Edge fromEdge;
  // fromLink, in fromViewport space
  public Point fromPoint;
  public RawViewport toViewport;
  // toLink, in toViewport space
  public Point toPoint;

  // Construct a link knowing only the from-end of the link.
  public Link(RawViewport fromViewport, Edge fromEdge, Point fromPoint) {
    this.fromViewport = fromViewport;
    this.fromEdge = fromEdge;
    this.fromPoint = fromPoint;
  }

  public Link(
      RawViewport fromViewport,
      Edge fromEdge,
      Point fromPoint,
      RawViewport toViewport,
      Point toPoint) {
    this.fromViewport = fromViewport;
    this.fromEdge = fromEdge;
    this.fromPoint = fromPoint;
    this.toViewport = toViewport;
    this.toPoint = toPoint;
  }

  public String toString() {
    return String.format(
        "Link(%s, %s, %s, %s, %s)", fromViewport, fromEdge, fromPoint, toViewport, toPoint);
  }

  // Returns the offset between the origin of fromViewport and the origin of toViewport
  // in absolute space. (How much to move the from to line it up with the to + edgeoffset).
  public Vector getOriginOffset() {
    Vector normalFrom = fromPoint.subtract(fromViewport.getExtent().origin());
    Vector normalTo = toPoint.subtract(toViewport.getExtent().origin());
    return normalFrom.subtract(normalTo).add(edgeLinkOffset());
  }

  // Returns the offset between a link fromPoint and toPoint given the fromEdge.
  private Vector edgeLinkOffset() {
    switch (fromEdge) {
      case LEFT:
        return Vector.create(-1, 0);
      case RIGHT:
        return Vector.create(1, 0);
      case TOP:
        return Vector.create(0, 1);
      case BOTTOM:
        return Vector.create(0, -1);
      default:
        throw new IllegalStateException("Invalid edge type");
    }
  }
}
