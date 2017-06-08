package com.salisburyclan.lpviewport.layout.linked;

//@RunWith(JUnit4.class)
public class LinkedPairsLayoutProviderTest {
  /*
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    @Mock private Viewport mockViewport1;
    @Mock private Viewport mockViewport2;
    @Mock private ViewportListener mockListener;

    private AggregateViewport viewport;

    @Before
    public void setUp() {
      // Two 10x20 Viewports, attached left-to-right.
      when(mockViewport1.getExtent()).thenReturn(Range2.create(0, 0, 9, 19));
      when(mockViewport2.getExtent()).thenReturn(Range2.create(10, 10, 19, 29));

      AggregateViewport.Builder builder = new AggregateViewport.Builder();
      builder.add(mockViewport1, Point.create(0, 0));
      builder.add(mockViewport2, Point.create(10, 0));
      viewport = builder.build();
    }

    @Test
    public void testSupportsSpec() throws Exception {
    public boolean supportsSpec(String layoutSpec) {
      assertThat(xxx).isEqualTo(xxx);
    }

    // Test that when we set lights on the aggregate viewport, the correct
    // sub-viewport lights get set.
    @Test
    public void testSetLight() throws Exception {
      public ListenableFuture<Viewport> createLayout(
              String layoutSpec, Collection<LaunchpadDevice> devices) {

    }

    // Test that when a sub-viewport button gets pressed, the correct
    // aggregate viewport button location gets notified.
    @Test
    public void testAddListener() throws Exception {
      ArgumentCaptor<ViewportListener> viewport1ListenerCaptor =
          ArgumentCaptor.forClass(ViewportListener.class);
      ArgumentCaptor<ViewportListener> viewport2ListenerCaptor =
          ArgumentCaptor.forClass(ViewportListener.class);

      viewport.addListener(mockListener);
      verify(mockViewport1).addListener(viewport1ListenerCaptor.capture());
      verify(mockViewport2).addListener(viewport2ListenerCaptor.capture());

      ViewportListener viewport1Listener = viewport1ListenerCaptor.getValue();
      ViewportListener viewport2Listener = viewport2ListenerCaptor.getValue();

      viewport1Listener.onButtonPressed(Point.create(0, 0));
      viewport1Listener.onButtonPressed(Point.create(9, 19));
      verify(mockListener).onButtonPressed(Point.create(0, 0));
      verify(mockListener).onButtonPressed(Point.create(9, 19));

      viewport2Listener.onButtonPressed(Point.create(10, 10));
      viewport2Listener.onButtonPressed(Point.create(19, 29));
      verify(mockListener).onButtonPressed(Point.create(10, 0));
      verify(mockListener).onButtonPressed(Point.create(19, 19));

      // TODO Check out of range
    }
  */
}

/*

package com.salisburyclan.lpviewport.layout;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.salisburyclan.lpviewport.animation.Animation;
import com.salisburyclan.lpviewport.animation.AnimationProvider;
import com.salisburyclan.lpviewport.animation.BorderSweep;
import com.salisburyclan.lpviewport.animation.EdgeSweep;
import com.salisburyclan.lpviewport.animation.Sweep;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Vector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkedPairsLayoutProvider implements LayoutProvider {

  private static final String TYPE = "linked";
  private static final String DESCRIPTION =
      TYPE + " : combines the viewports by user selecting attachment points";
  private static final AnimationProvider AWAITING_SELECTION_ANIMATION =
      BorderSweep.newProvider(Color.RED);
  private static final AnimationProvider SELECTED_VIEWPORT_ANIMATION =
      Sweep.newProvider(Color.BLUE, false);

  @Override
  public List<String> getLayoutSpecDescriptions() {
    return ImmutableList.of(DESCRIPTION);
  }

  @Override
  public boolean supportsSpec(String layoutSpec) {
    return TYPE.equals(layoutSpec);
  }

  @Override
  public ListenableFuture<Viewport> createLayout(
      String layoutSpec, Collection<LaunchpadDevice> devices) {
    if (!TYPE.equals(layoutSpec)) {
      throw new IllegalArgumentException("Invalid viewportSpec for " + getClass().getName());
    }
    List<Viewport> viewports =
        devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList());

    if (viewports.isEmpty()) {
      throw new IllegalArgumentException("Empty LayoutSpec: " + layoutSpec);
    } else if (viewports.size() == 1) {
      return Futures.immediateFuture(viewports.get(0));
    } else {
      return makeLinkedViewport(
          devices.stream().map(LaunchpadDevice::getViewport).collect(Collectors.toList()));
    }
  }

  private ListenableFuture<Viewport> makeLinkedViewport(List<Viewport> viewports) {
    return new ViewportBuilder(viewports).getFutureViewport();
  }

  private static class ViewportBuilder {
    private SettableFuture<Viewport> futureViewport;
    private List<Viewport> unselectedViewports;
    private List<Viewport> selectedViewports;
    private List<Runnable> viewportCleanups;
    private LinkAssembler linkAssembler;

    // Represents a link between two viewports.
    // (fromX,fromY) must be along edge of fromViewport.
    // (toX,toY) must be along opposite edge of toViewport.
    private static class Link {
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

    public ViewportBuilder(List<Viewport> viewports) {
      this.futureViewport = SettableFuture.create();
      this.unselectedViewports = new ArrayList<>();
      this.selectedViewports = new ArrayList<>();
      this.linkAssembler = new LinkAssembler();
      this.viewportCleanups = new ArrayList<>();
      // First choose edge of any Viewport
      viewports.forEach(
          viewport -> {
            unselectedViewports.add(viewport);
            waitForLinkStart(viewport);
          });
    }

    // Accept a click on any edge and then wait for linked click.
    private void waitForLinkStart(Viewport viewport) {
      Animation animation = AWAITING_SELECTION_ANIMATION.newAnimation(viewport);
      animation.play();
      ViewportListener listener =
          new ViewportListener() {
            public void onButtonPressed(Point p) {
              if (Edge.getEdge(viewport.getExtent(), p) != Edge.INVALID) {
                deactivateViewports();
                selectLinkStart(viewport, p);
              }
            }

            public void onButtonReleased(Point p) {}
          };
      viewport.addListener(listener);
      viewportCleanups.add(
          () -> {
            animation.stop();
            viewport.setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private void deactivateViewports() {
      viewportCleanups.forEach(Runnable::run);
      viewportCleanups.clear();
    }

    private void selectLinkStart(Viewport startViewport, Point start) {
      // if this is the first selection, startViewport needs to be moved
      // from unselected to selected.
      if (unselectedViewports.remove(startViewport)) {
        selectedViewports.add(startViewport);
      }
      Edge selectedEdge = Edge.getEdge(startViewport.getExtent(), start);
      Edge oppositeEdge = selectedEdge.getOpposite();
      Link partialLink = new Link(startViewport, selectedEdge, start);
      unselectedViewports.forEach(
          viewport -> {
            waitForLinkEnd(viewport, oppositeEdge, partialLink);
          });
    }

    // Accept a click on only one edge and register the link.
    private void waitForLinkEnd(Viewport viewport, Edge requiredEdge, Link partialLink) {
      Animation animation = new EdgeSweep(viewport, requiredEdge, Color.RED);
      animation.play();
      ViewportListener listener =
          new ViewportListener() {
            public void onButtonPressed(Point p) {
              if (requiredEdge.isEdge(viewport.getExtent(), p)) {
                deactivateViewports();
                selectLinkEnd(partialLink, viewport, p);
              }
            }

            public void onButtonReleased(Point p) {}
          };
      viewport.addListener(listener);
      viewportCleanups.add(
          () -> {
            animation.stop();
            viewport.setAllLights(Color.BLACK);
            viewport.removeListener(listener);
          });
    }

    private void selectLinkEnd(Link partialLink, Viewport endViewport, Point endPoint) {
      unselectedViewports.remove(endViewport);
      selectedViewports.add(endViewport);

      partialLink.toViewport = endViewport;
      partialLink.toPoint = endPoint;
      linkAssembler.addLink(partialLink);

      if (unselectedViewports.isEmpty()) {
        finalizeViewport();
      } else {
        selectedViewports.forEach(
            viewport -> {
              waitForLinkStart(viewport);
            });
      }
    }

    private void finalizeViewport() {
      Viewport finalViewport = linkAssembler.build();
      //      SELECTED_VIEWPORT_ANIMATION.newAnimation(finalViewport).play();
      futureViewport.set(finalViewport);
    }

    public ListenableFuture<Viewport> getFutureViewport() {
      return futureViewport;
    }

    // Constructs a Viewport given the links between subviewports.
    private static class LinkAssembler {
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
  }
}
*/
