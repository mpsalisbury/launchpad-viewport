package com.salisburyclan.lpviewport.layout.linked;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.layout.AggregateViewport;
import java.util.HashMap;
import java.util.Map;

// Constructs a Viewport given the links between subviewports.
public class LinkAssembler {
  private Map<Viewport, Point> viewportOrigins;
  private AggregateViewport.Builder viewportBuilder;

  public LinkAssembler() {
    this.viewportOrigins = new HashMap<>();
    this.viewportBuilder = new AggregateViewport.Builder();
  }

  public void addLink(Link link) {
    if (viewportOrigins.isEmpty()) {
      addViewport(link.fromViewport, Point.create(0, 0));
    }
    Point toViewportOrigin = computeToViewportOrigin(link);
    addViewport(link.toViewport, toViewportOrigin);
  }

  private void addViewport(Viewport viewport, Point origin) {
    viewportBuilder.add(viewport, origin);
    viewportOrigins.put(viewport, origin);
  }

  private Point computeToViewportOrigin(Link link) {
    Point fromOrigin = viewportOrigins.get(link.fromViewport);
    Vector originOffset = link.getOriginOffset();
    return fromOrigin.add(originOffset);
  }

  public Viewport build() {
    return viewportBuilder.build();
  }
}
