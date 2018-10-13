package com.salisburyclan.lpviewport.viewport;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range1;
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
public class SubViewport1Test {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Button1Listener mockListener;

  private static final Range2 EXTENT = Range2.create(0, 0, 9, 9);

  // Vertical extent within EXTENT.
  private static final Range2 VERTICAL_EXTENT = Range2.create(3, 2, 3, 7);
  // Point within vertical extent.
  private static final int VERTICAL_TEST_POINT = 4;
  // Point within overall EXTENT corresponding to VERTICAL_TEST_POINT.
  private static final Point VERTICAL_TEST_BASE_POINT = Point.create(3, 4);

  // Horizontal extent within EXTENT.
  private static final Range2 HORIZONTAL_EXTENT = Range2.create(2, 3, 7, 3);
  // Point within horizontal extent.
  private static final int HORIZONTAL_TEST_POINT = 4;
  // Point within overall EXTENT corresponding to HORIZONTAL_TEST_POINT.
  private static final Point HORIZONTAL_TEST_BASE_POINT = Point.create(4, 3);

  private static final Pixel PIXEL = Pixel.create(Color.ORANGE);
  private TestViewport baseViewport;
  private Viewport1 horizontalViewport;
  private Viewport1 verticalViewport;

  @Before
  public void setUp() {
    baseViewport = new TestViewport(EXTENT);
    horizontalViewport = new SubViewport1(baseViewport, HORIZONTAL_EXTENT);
    verticalViewport = new SubViewport1(baseViewport, VERTICAL_EXTENT);
  }

  @Test
  public void testInvalidConstruction() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport1(baseViewport, Range2.create(1, 1, 3, 3)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport1(baseViewport, Range2.create(-1, 0, 2, 9)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport1(baseViewport, Range2.create(7, 0, 10, 9)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport1(baseViewport, Range2.create(0, -1, 9, 2)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport1(baseViewport, Range2.create(0, 7, 9, 10)));
  }

  @Test
  public void testGetExtent() {
    assertThat(horizontalViewport.getExtent()).isEqualTo(HORIZONTAL_EXTENT.xRange());
    assertThat(verticalViewport.getExtent()).isEqualTo(VERTICAL_EXTENT.yRange());
  }

  @Test
  public void testSetPixel() {
    testSetPixel(verticalViewport, VERTICAL_TEST_POINT, VERTICAL_TEST_BASE_POINT);
    testSetPixel(horizontalViewport, HORIZONTAL_TEST_POINT, HORIZONTAL_TEST_BASE_POINT);
  }

  public void testSetPixel(Viewport1 viewport1, int point, Point basePoint) {
    viewport1.setPixel(point, PIXEL);
    assertThat(baseViewport.getPixel(basePoint)).isEqualTo(PIXEL);
  }

  @Test
  public void testSetPixelOutOfRange() {
    testSetPixelOutOfRange(verticalViewport, HORIZONTAL_EXTENT.xRange());
    testSetPixelOutOfRange(horizontalViewport, VERTICAL_EXTENT.yRange());
  }

  public void testSetPixelOutOfRange(Viewport1 viewport1, Range1 range) {
    assertThrows(IllegalArgumentException.class, () -> viewport1.setPixel(range.low() - 1, PIXEL));
    assertThrows(IllegalArgumentException.class, () -> viewport1.setPixel(range.high() + 1, PIXEL));
    // doesn't throw
    viewport1.setPixel(range.low(), PIXEL);
    viewport1.setPixel(range.high(), PIXEL);
  }

  @Test
  public void testListener() {
    testListener(verticalViewport, VERTICAL_TEST_POINT, VERTICAL_TEST_BASE_POINT);
    testListener(horizontalViewport, HORIZONTAL_TEST_POINT, HORIZONTAL_TEST_BASE_POINT);
  }

  public void testListener(Viewport1 viewport1, int point, Point basePoint) {
    reset(mockListener);
    viewport1.addListener(mockListener);
    baseViewport.pushButton(basePoint);
    verify(mockListener).onButtonPressed(point);

    baseViewport.releaseButton(basePoint);
    verify(mockListener).onButtonReleased(point);

    baseViewport.pushButton(basePoint.add(Vector.create(1, 1)));
    verifyZeroInteractions(mockListener);

    viewport1.removeListener(mockListener);
    baseViewport.pushButton(basePoint);
    verifyZeroInteractions(mockListener);

    // Test removeAllListeners
    reset(mockListener);
    viewport1.addListener(mockListener);
    baseViewport.pushButton(basePoint);
    verify(mockListener).onButtonPressed(point);

    viewport1.removeAllListeners();
    baseViewport.pushButton(basePoint);
    verifyZeroInteractions(mockListener);
  }
}
