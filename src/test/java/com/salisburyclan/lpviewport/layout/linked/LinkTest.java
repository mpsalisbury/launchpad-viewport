package com.salisburyclan.lpviewport.layout.linked;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class LinkTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Viewport mockViewport1;
  @Mock private Viewport mockViewport2;

  public void setupViewport(Viewport mockViewport, Range2 range) {
    when(mockViewport.getExtent()).thenReturn(range);
  }

  @Test
  public void testOriginOffsetRight() throws Exception {
    // Link upper corners.
    setupViewport(mockViewport1, Range2.create(0, 0, 9, 9));
    setupViewport(mockViewport2, Range2.create(0, 0, 9, 9));
    Link link =
        new Link(mockViewport1, Edge.RIGHT, Point.create(9, 0), mockViewport2, Point.create(0, 0));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(10, 0));
  }

  @Test
  public void testOriginOffsetLeft() throws Exception {
    // Link upper corners.
    setupViewport(mockViewport1, Range2.create(0, 0, 9, 9));
    setupViewport(mockViewport2, Range2.create(0, 0, 9, 9));
    Link link =
        new Link(mockViewport1, Edge.LEFT, Point.create(0, 0), mockViewport2, Point.create(9, 0));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(-10, 0));
  }

  @Test
  public void testOriginOffsetBottom() throws Exception {
    // Link upper corners.
    setupViewport(mockViewport1, Range2.create(0, 0, 9, 9));
    setupViewport(mockViewport2, Range2.create(0, 0, 9, 9));
    Link link =
        new Link(mockViewport1, Edge.BOTTOM, Point.create(0, 0), mockViewport2, Point.create(0, 9));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(0, -10));
  }

  @Test
  public void testOriginOffsetTop() throws Exception {
    // Link upper corners.
    setupViewport(mockViewport1, Range2.create(0, 0, 9, 9));
    setupViewport(mockViewport2, Range2.create(0, 0, 9, 9));
    Link link =
        new Link(mockViewport1, Edge.TOP, Point.create(0, 9), mockViewport2, Point.create(0, 0));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(0, 10));
  }

  @Test
  public void testOriginOffsetMiddleEdge() throws Exception {
    setupViewport(mockViewport1, Range2.create(0, 0, 9, 9));
    setupViewport(mockViewport2, Range2.create(0, 0, 9, 9));
    Link link =
        new Link(mockViewport1, Edge.RIGHT, Point.create(9, 5), mockViewport2, Point.create(0, 5));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(10, 0));
  }

  @Test
  public void testOriginOffsetNonZeroOrigin() throws Exception {
    setupViewport(mockViewport1, Range2.create(2, 2, 11, 11));
    setupViewport(mockViewport2, Range2.create(12, 4, 21, 9));
    Link link =
        new Link(
            mockViewport1, Edge.RIGHT, Point.create(11, 2), mockViewport2, Point.create(12, 4));
    assertThat(link.getOriginOffset()).isEqualTo(Vector.create(10, 0));
  }
}
