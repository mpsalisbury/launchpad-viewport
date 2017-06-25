package com.salisburyclan.lpviewport.layout;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.RawLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
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

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT);
  @Mock private RawViewport mockViewport1;
  @Mock private RawViewport mockViewport2;
  @Mock private RawLayer mockRawLayer1;
  @Mock private RawLayer mockRawLayer2;
  @Mock private Button2Listener mockListener;

  private RawViewport viewport;

  @Before
  public void setUp() {
    // Two 10x20 Viewports, attached left-to-right.
    when(mockViewport1.getExtent()).thenReturn(Range2.create(0, 0, 9, 19));
    when(mockViewport2.getExtent()).thenReturn(Range2.create(10, 10, 19, 29));
    when(mockViewport1.getRawLayer()).thenReturn(mockRawLayer1);
    when(mockViewport2.getRawLayer()).thenReturn(mockRawLayer2);

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
    RawLayer layer = viewport.getRawLayer();
    DColor color = DColor.RED;
    layer.setPixel(Point.create(0, 0), color);
    layer.setPixel(Point.create(9, 19), color);
    verify(mockRawLayer1).setPixel(Point.create(0, 0), color);
    verify(mockRawLayer1).setPixel(Point.create(9, 19), color);

    layer.setPixel(Point.create(10, 0), color);
    layer.setPixel(Point.create(19, 19), color);
    verify(mockRawLayer2).setPixel(Point.create(10, 10), color);
    verify(mockRawLayer2).setPixel(Point.create(19, 29), color);

    // TODO Check out of range
  }

  // Test that when a sub-viewport button gets pressed, the correct
  // aggregate viewport button location gets notified.
  @Test
  public void testAddListener() throws Exception {
    ArgumentCaptor<Button2Listener> viewport1ListenerCaptor =
        ArgumentCaptor.forClass(Button2Listener.class);
    ArgumentCaptor<Button2Listener> viewport2ListenerCaptor =
        ArgumentCaptor.forClass(Button2Listener.class);

    viewport.addListener(mockListener);
    verify(mockViewport1).addListener(viewport1ListenerCaptor.capture());
    verify(mockViewport2).addListener(viewport2ListenerCaptor.capture());

    Button2Listener viewport1Listener = viewport1ListenerCaptor.getValue();
    Button2Listener viewport2Listener = viewport2ListenerCaptor.getValue();

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
