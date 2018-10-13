package com.salisburyclan.lpviewport.viewport;

import static com.google.common.truth.Truth.assertThat;
import static com.salisburyclan.lpviewport.testing.AssertThrows.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Pixel;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
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
public class SubViewportTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Button2Listener mockListener;

  private static final Range2 BASE_EXTENT = Range2.create(0, 0, 9, 9);
  private static final Range2 SUB_EXTENT = Range2.create(2, 3, 7, 6);
  private static final Point POINT = Point.create(4, 5);

  private static final Pixel PIXEL = Pixel.create(Color.ORANGE);
  private TestViewport baseViewport;
  private Viewport viewport;

  @Before
  public void setUp() {
    baseViewport = new TestViewport(BASE_EXTENT);
    viewport = new SubViewport(baseViewport, SUB_EXTENT);
  }

  @Test
  public void testInvalidConstruction() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport(baseViewport, Range2.create(-1, 0, 9, 9)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport(baseViewport, Range2.create(0, -1, 9, 9)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport(baseViewport, Range2.create(0, 0, 10, 9)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SubViewport(baseViewport, Range2.create(0, 0, 0, 10)));
  }

  @Test
  public void testSetPixel() {
    WriteLayer layer = viewport.addLayer();
    layer.setPixel(POINT, PIXEL);
    assertThat(baseViewport.getPixel(POINT)).isEqualTo(PIXEL);
  }

  @Test
  public void testListener() {
    viewport.addListener(mockListener);
    baseViewport.pushButton(POINT);
    verify(mockListener).onButtonPressed(POINT);

    baseViewport.releaseButton(POINT);
    verify(mockListener).onButtonReleased(POINT);

    baseViewport.pushButton(Point.create(-1, -1));
    verifyZeroInteractions(mockListener);

    viewport.removeListener(mockListener);
    baseViewport.pushButton(POINT);
    verifyZeroInteractions(mockListener);
  }

  @Test
  public void testRemoveAllListeners() {
    viewport.addListener(mockListener);
    baseViewport.pushButton(POINT);
    verify(mockListener).onButtonPressed(POINT);

    viewport.removeAllListeners();
    baseViewport.pushButton(POINT);
    verifyZeroInteractions(mockListener);
  }
}

/*
  TODO: Test these:
  public Range2 getExtent() {
  public LayerBuffer addLayer() {
  public void addLayer(ReadLayer layer) {
  public void removeLayer(ReadLayer layer) {
*/
