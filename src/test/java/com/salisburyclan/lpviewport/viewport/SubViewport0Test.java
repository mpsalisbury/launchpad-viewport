package com.salisburyclan.lpviewport.viewport;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport0;
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
public class SubViewport0Test {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Button0Listener mockListener;

  private static final Range2 EXTENT = Range2.create(0, 0, 9, 9);
  private static final Pixel PIXEL = Pixel.create(Color.ORANGE);
  private TestViewport baseViewport;
  private Point point;
  private Viewport0 viewport0;

  @Before
  public void setUp() {
    baseViewport = new TestViewport(EXTENT);
    point = Point.create(1, 1);
    viewport0 = new SubViewport0(baseViewport, point);
  }

  @Test
  public void testInvalidConstruction() {
    assertThrows(
        IllegalArgumentException.class, () -> new SubViewport0(baseViewport, Point.create(-1, 0)));
    assertThrows(
        IllegalArgumentException.class, () -> new SubViewport0(baseViewport, Point.create(0, -1)));
    assertThrows(
        IllegalArgumentException.class, () -> new SubViewport0(baseViewport, Point.create(10, 0)));
    assertThrows(
        IllegalArgumentException.class, () -> new SubViewport0(baseViewport, Point.create(0, 10)));
  }

  @Test
  public void testSetPixel() {
    viewport0.setPixel(PIXEL);
    assertThat(baseViewport.getPixel(point)).isEqualTo(PIXEL);
  }

  @Test
  public void testListener() {
    viewport0.addListener(mockListener);
    baseViewport.pushButton(point);
    verify(mockListener).onButtonPressed();

    baseViewport.pushButton(point.add(Vector.create(1, 1)));
    verifyZeroInteractions(mockListener);

    viewport0.removeListener(mockListener);
    baseViewport.pushButton(point);
    verifyZeroInteractions(mockListener);
  }
}
