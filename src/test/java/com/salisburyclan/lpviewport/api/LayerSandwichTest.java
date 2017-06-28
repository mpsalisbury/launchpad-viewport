package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.util.CloseListenerMultiplexer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class LayerSandwichTest {
  private static final Pixel BLACK = Pixel.create(Color.BLACK);
  private static final Pixel HALF_WHITE = Pixel.create(Color.WHITE, 0.5);
  private static final Range2 FULL_EXTENT = Range2.create(0, 0, 9, 9);
  private static final Range2 HALF_EXTENT = Range2.create(5, 0, 9, 9);

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private PixelListener mockPixelListener;
  @Mock private CloseListener mockCloseListener;

  private Pixel gray(double intensity) {
    return Pixel.create(Color.create(intensity, intensity, intensity));
  }

  @Test
  public void testLayerVisibility() {
    ReadLayer backgroundLayer = new FlatLayer(FULL_EXTENT, BLACK);
    // Covers full viewport
    ReadLayer wholeLayer = new FlatLayer(FULL_EXTENT, HALF_WHITE);
    // Covers right half of viewport
    ReadLayer halfLayer = new FlatLayer(HALF_EXTENT, HALF_WHITE);

    LayerSandwich sandwich = new LayerSandwich(FULL_EXTENT);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);

    // background only
    sandwich.addLayer(backgroundLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.0));

    // background + whole
    sandwich.addLayer(wholeLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.5));

    // background + whole + half
    sandwich.addLayer(halfLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.5));
    assertThat(sandwich.getPixel(5, 0)).isEqualTo(gray(0.75));

    // leaves background + half
    sandwich.removeLayer(wholeLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.0));
    assertThat(sandwich.getPixel(5, 0)).isEqualTo(gray(0.5));

    // leaves background
    sandwich.removeLayer(halfLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.0));
    assertThat(sandwich.getPixel(5, 0)).isEqualTo(gray(0.0));

    // leaves no layers
    sandwich.removeLayer(backgroundLayer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);
    assertThat(sandwich.getPixel(5, 0)).isEqualTo(Pixel.EMPTY);
  }

  @Test
  public void testClose() {
    FlatLayer layer = new FlatLayer(FULL_EXTENT, gray(0.6));

    LayerSandwich sandwich = new LayerSandwich(FULL_EXTENT);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);

    sandwich.addLayer(layer);
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(gray(0.6));

    layer.close();
    assertThat(sandwich.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);
  }

  @Test
  public void testPixelListeners() {
    LayerBuffer underLayer = new LayerBuffer(FULL_EXTENT);
    LayerBuffer overLayer = new LayerBuffer(FULL_EXTENT);
    Pixel pixel = gray(0.6);

    LayerSandwich sandwich = new LayerSandwich(FULL_EXTENT);
    underLayer.setPixel(0, 0, pixel);

    sandwich.addPixelListener(mockPixelListener);

    // Just underLayer;
    sandwich.addLayer(underLayer);
    underLayer.setPixel(0, 1, pixel);
    verify(mockPixelListener).onSetPixel(0, 1);

    // Underlayer + overlayer.
    sandwich.addLayer(overLayer);
    overLayer.setPixel(0, 2, pixel);
    verify(mockPixelListener).onSetPixel(0, 2);
    underLayer.setPixel(0, 3, pixel);
    verify(mockPixelListener).onSetPixel(0, 3);

    // Remove listener
    sandwich.removePixelListener(mockPixelListener);
    overLayer.setPixel(0, 4, pixel);
    underLayer.setPixel(0, 5, pixel);

    // Replace listener, remove underlayer.
    sandwich.addPixelListener(mockPixelListener);
    sandwich.removeLayer(underLayer);
    overLayer.setPixel(0, 6, pixel);
    verify(mockPixelListener).onSetPixel(0, 6);
    underLayer.setPixel(0, 7, pixel);

    // Remove overlayer too.  No layers remaining.
    sandwich.removeLayer(overLayer);
    overLayer.setPixel(0, 8, pixel);
    underLayer.setPixel(0, 9, pixel);

    verifyNoMoreInteractions(mockPixelListener);
  }

  @Test
  public void testCloseListeners() {
    LayerSandwich sandwich = new LayerSandwich(FULL_EXTENT);
    sandwich.addCloseListener(mockCloseListener);
    sandwich.close();

    verify(mockCloseListener).onClose();
    verifyNoMoreInteractions(mockCloseListener);
  }

  // TODO Consider moving someplace public.
  private class FlatLayer implements ReadLayer {
    private Range2 extent;
    private Pixel pixel;
    private CloseListenerMultiplexer closeListeners;

    public FlatLayer(Range2 extent, Pixel pixel) {
      this.extent = extent;
      this.pixel = pixel;
      this.closeListeners = new CloseListenerMultiplexer();
    }

    public Range2 getExtent() {
      return extent;
    }

    public Pixel getPixel(int x, int y) {
      if (extent.isPointWithin(x, y)) {
        return pixel;
      } else {
        return Pixel.EMPTY;
      }
    }

    public void close() {
      closeListeners.onClose();
    }

    public void addPixelListener(PixelListener listener) {
      // Pixels don't change. Do nothing.
    }

    public void removePixelListener(PixelListener listener) {
      // Pixels don't change. Do nothing.
    }

    public void addCloseListener(CloseListener listener) {
      closeListeners.add(listener);
    }
  }
}
