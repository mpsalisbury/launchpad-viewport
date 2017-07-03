package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class LayerBufferTest {
  private static final Pixel BLACK = Pixel.create(Color.BLACK);
  private static final Pixel HALF_WHITE = Pixel.create(Color.WHITE, 0.5);
  private static final Range2 EXTENT = Range2.create(0, 0, 9, 9);

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private PixelListener mockPixelListener;
  @Mock private CloseListener mockCloseListener;

  @Test
  public void testPixelListeners() {
    LayerBuffer layer = new LayerBuffer(EXTENT);
    Pixel pixel = Pixel.create(Color.create(0.1, 0.2, 0.3));

    InOrder inOrder = inOrder(mockPixelListener);

    layer.addPixelListener(mockPixelListener);
    layer.setPixel(0, 0, pixel);
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));

    layer.nextFrame();
    inOrder.verify(mockPixelListener).onNextFrame();
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));

    layer.removePixelListener(mockPixelListener);
    layer.setPixel(0, 1, pixel);

    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testCloseListeners() {
    LayerBuffer layer = new LayerBuffer(EXTENT);

    layer.addCloseListener(mockCloseListener);
    layer.close();
    verify(mockCloseListener).onClose();

    verifyNoMoreInteractions(mockCloseListener);
  }

  @Test
  public void testGetExtent() {
    LayerBuffer layer = new LayerBuffer(EXTENT);
    assertThat(layer.getExtent()).isEqualTo(EXTENT);
  }

  @Test
  public void testPixels() {
    LayerBuffer layer = new LayerBuffer(EXTENT);
    Pixel pixel = Pixel.create(Color.RED, 0.3);

    // Default pixel.
    assertThat(layer.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);
    // Out of extent pixel.
    assertThat(layer.getPixel(-1, 0)).isEqualTo(Pixel.EMPTY);

    layer.setPixel(0, 0, Color.BROWN);
    layer.setPixel(0, 1, pixel);
    assertThat(layer.getPixel(0, 0)).isEqualTo(Pixel.create(Color.BROWN));
    assertThat(layer.getPixel(0, 1)).isEqualTo(pixel);
  }

  private Pixel alphaPixel(double alpha) {
    return Pixel.create(Color.WHITE, alpha);
  }

  @Test
  public void testCombinePixel() {
    LayerBuffer layer = new LayerBuffer(EXTENT);

    assertThat(layer.getPixel(0, 0)).isEqualTo(alphaPixel(0.0));

    layer.combinePixel(0, 0, alphaPixel(0.5));
    assertThat(layer.getPixel(0, 0)).isEqualTo(alphaPixel(0.5));

    layer.combinePixel(0, 0, alphaPixel(0.5));
    assertThat(layer.getPixel(0, 0)).isEqualTo(alphaPixel(0.75));

    layer.combinePixel(0, 0, alphaPixel(0.5));
    assertThat(layer.getPixel(0, 0)).isEqualTo(alphaPixel(0.875));
  }
}
