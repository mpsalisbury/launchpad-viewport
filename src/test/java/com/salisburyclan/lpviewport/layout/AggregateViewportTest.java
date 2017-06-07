package com.salisburyclan.lpviewport.layout;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class AggregateViewportTest {

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
  public void testGetExtent() throws Exception {
    assertThat(viewport.getExtent()).isEqualTo(Range2.create(0, 0, 19, 19));
  }

  // Test that when we set lights on the aggregate viewport, the correct
  // sub-viewport lights get set.
  @Test
  public void testSetLight() throws Exception {
    Color color = Color.RED;
    viewport.setLight(Point.create(0, 0), color);
    viewport.setLight(Point.create(9, 19), color);
    verify(mockViewport1).setLight(Point.create(0, 0), color);
    verify(mockViewport1).setLight(Point.create(9, 19), color);

    viewport.setLight(Point.create(10, 0), color);
    viewport.setLight(Point.create(19, 19), color);
    verify(mockViewport2).setLight(Point.create(10, 10), color);
    verify(mockViewport2).setLight(Point.create(19, 29), color);

    // TODO Check out of range
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
}
