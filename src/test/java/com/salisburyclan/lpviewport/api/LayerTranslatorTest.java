package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.geom.Vector;
import com.salisburyclan.lpviewport.testing.FlatLayer;
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
public class LayerTranslatorTest {
  private static final Pixel WHITE = Pixel.create(Color.WHITE);
  private static final Pixel BLACK = Pixel.create(Color.BLACK);
  private static final Range2 EXTENT = Range2.create(0, 0, 1, 1);

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private PixelListener mockPixelListener;
  @Mock private CloseListener mockCloseListener;

  @Test
  public void testGetPixel() {
    LayerTranslator translator = new LayerTranslator(new FlatLayer(EXTENT, WHITE));
    // No translation
    assertPixelsPresent(translator, 1, 1, 1, 1);

    // Left/Bottom overlap.
    translator.setOffset(Vector.create(-1, -1));
    assertPixelsPresent(translator, 1, 0, 0, 0);

    // Right/Top overlap.
    translator.setOffset(Vector.create(1, 1));
    assertPixelsPresent(translator, 0, 0, 0, 1);

    // No overlap.
    translator.setOffset(Vector.create(2, 0));
    assertPixelsPresent(translator, 0, 0, 0, 0);
  }

  // Asserts whether the given pixels are present (1) or empty (0) from
  // a 2x2 window (0,0,1,1).
  private void assertPixelsPresent(ReadLayer layer, int p00, int p01, int p10, int p11) {
    assertPixelPresent(layer.getPixel(0, 0), p00);
    assertPixelPresent(layer.getPixel(0, 1), p01);
    assertPixelPresent(layer.getPixel(1, 0), p10);
    assertPixelPresent(layer.getPixel(1, 1), p11);
  }

  private void assertPixelPresent(Pixel pixel, int expectPresent) {
    if (expectPresent == 1) {
      assertThat(pixel).isNotEqualTo(Pixel.EMPTY);
    } else {
      assertThat(pixel).isEqualTo(Pixel.EMPTY);
    }
  }

  @Test
  public void testAddOffset() {
    LayerTranslator translator = new LayerTranslator(new FlatLayer(EXTENT, WHITE));
    assertThat(translator.getOffset()).isEqualTo(Vector.create(0, 0));
    assertThat(translator.getExtent()).isEqualTo(Range2.create(0, 0, 1, 1));

    translator.addOffset(Vector.create(1, 0));
    assertThat(translator.getOffset()).isEqualTo(Vector.create(1, 0));
    assertThat(translator.getExtent()).isEqualTo(Range2.create(1, 0, 2, 1));

    translator.addOffset(Vector.create(1, 0));
    assertThat(translator.getOffset()).isEqualTo(Vector.create(2, 0));
    assertThat(translator.getExtent()).isEqualTo(Range2.create(2, 0, 3, 1));

    translator.addOffset(Vector.create(-2, -2));
    assertThat(translator.getOffset()).isEqualTo(Vector.create(0, -2));
    assertThat(translator.getExtent()).isEqualTo(Range2.create(0, -2, 1, -1));
  }

  @Test
  public void testPixelListeners() {
    ReadWriteLayer innerLayer = new LayerBuffer(EXTENT);
    LayerTranslator translator = new LayerTranslator(innerLayer);

    InOrder inOrder = inOrder(mockPixelListener);
    translator.addPixelListener(mockPixelListener);

    innerLayer.setPixel(0, 0, WHITE);
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));

    // Translate (1,1), pixelsChanged covers (0,0,1,1) and (1,1,2,2).
    translator.setOffset(Vector.create(1, 1));
    inOrder.verify(mockPixelListener).onPixelsChanged(Range2.create(0, 0, 2, 2));

    // Set to different color or LayerBuffer won't notify of changed value.
    innerLayer.setPixel(0, 0, BLACK);
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(1, 1));

    translator.removePixelListener(mockPixelListener);
    innerLayer.setPixel(0, 1, WHITE);

    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testCloseListeners() {
    ReadWriteLayer innerLayer = new LayerBuffer(EXTENT);
    LayerTranslator translator = new LayerTranslator(innerLayer);

    translator.addCloseListener(mockCloseListener);
    innerLayer.close();
    verify(mockCloseListener).onClose();

    verifyNoMoreInteractions(mockCloseListener);
  }
}
