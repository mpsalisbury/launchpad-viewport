package com.salisburyclan.lpviewport.viewport;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.testing.TestViewport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class StripSubViewport0Test {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Button0Listener mockListener;

  private static final Range2 EXTENT = Range2.create(0, 0, 9, 9);
  private static final Range2 EXTENT1 = Range2.create(0, 2, 9, 2);
  // Point within baseViewport
  private static final Point POINT = Point.create(2, 2);
  private static final Pixel PIXEL = Pixel.create(Color.ORANGE);
  private TestViewport baseViewport;

  private Viewport0 viewport0;

  @Before
  public void setUp() {
    baseViewport = new TestViewport(EXTENT);
    Viewport1 baseViewport1 = new SubViewport1(baseViewport, EXTENT1);
    viewport0 = new StripSubViewport0(baseViewport1, POINT.x());
  }

  @Test
  public void testInvalidConstruction() {
    Viewport1 baseViewport1 = new SubViewport1(baseViewport, EXTENT1);
    assertThrows(IllegalArgumentException.class, () -> new StripSubViewport0(baseViewport1, -1));
    assertThrows(IllegalArgumentException.class, () -> new StripSubViewport0(baseViewport1, 10));
  }

  @Test
  public void testSetPixel() {
    viewport0.setPixel(PIXEL);
    assertThat(baseViewport.getPixel(POINT)).isEqualTo(PIXEL);
  }

  @Test
  public void testListener() {
    viewport0.addListener(mockListener);
    baseViewport.pushButton(POINT);
    verify(mockListener).onButtonPressed();

    baseViewport.releaseButton(POINT);
    verify(mockListener).onButtonReleased();

    baseViewport.pushButton(POINT.add(Vector.create(1, 1)));
    verifyZeroInteractions(mockListener);

    viewport0.removeListener(mockListener);
    baseViewport.pushButton(POINT);
    verifyZeroInteractions(mockListener);
  }
}
