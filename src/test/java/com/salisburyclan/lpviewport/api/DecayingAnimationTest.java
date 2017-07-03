package com.salisburyclan.lpviewport.api;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import org.junit.Before;
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
public class DecayingAnimationTest {
  private static final Pixel WHITE = Pixel.create(Color.WHITE);
  private static final Pixel HALF_WHITE = Pixel.create(Color.WHITE, 0.5);
  private static final Range2 EXTENT = Range2.create(0, 0, 9, 9);

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private PixelListener mockPixelListener;
  @Mock private CloseListener mockCloseListener;

  private LayerBuffer buffer;
  private DecayingAnimation.SimpleTickProvider tickProvider;
  private DecayingAnimation animation;
  private Pixel pixel;

  @Before
  public void setUp() {
    buffer = new LayerBuffer(EXTENT);
    tickProvider = new DecayingAnimation.SimpleTickProvider();
    animation = new DecayingAnimation(buffer, tickProvider, 0.5);
    pixel = Pixel.create(Color.create(0.1, 0.2, 0.3));
  }

  @Test
  public void testDecay() {
    assertThat(animation.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);
    buffer.setPixel(0, 0, WHITE);
    assertThat(animation.getPixel(0, 0)).isEqualTo(WHITE);

    tickProvider.fireTick();
    assertThat(animation.getPixel(0, 0)).isEqualTo(WHITE);

    buffer.nextFrame();
    assertThat(animation.getPixel(0, 0)).isEqualTo(WHITE);

    tickProvider.fireTick();
    assertThat(animation.getPixel(0, 0)).isEqualTo(HALF_WHITE);

    tickProvider.fireTick();
    assertThat(animation.getPixel(0, 0)).isEqualTo(Pixel.EMPTY);
  }

  @Test
  public void testGetExtent() {
    assertThat(animation.getExtent()).isEqualTo(EXTENT);
  }

  @Test
  public void testPixelListeners() {
    InOrder inOrder = inOrder(mockPixelListener);
    animation.addPixelListener(mockPixelListener);

    tickProvider.fireTick();
    // no pixel change

    buffer.setPixel(0, 0, pixel);
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));

    buffer.nextFrame();
    // Updates twice, once due to updating decay layer with copy, and
    // once due to clearing input layer.
    inOrder.verify(mockPixelListener, times(2)).onPixelChanged(Point.create(0, 0));

    tickProvider.fireTick();
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));
    tickProvider.fireTick();
    inOrder.verify(mockPixelListener).onPixelChanged(Point.create(0, 0));

    tickProvider.fireTick();
    // no pixel change for third tick

    animation.removePixelListener(mockPixelListener);
    // no more pixel changes reported
    buffer.setPixel(0, 1, pixel);
    buffer.nextFrame();
    tickProvider.fireTick();

    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testCloseListener() {
    animation.addCloseListener(mockCloseListener);
    // DecayingAnimation closes on first tick after decay is complete and input layer is closed.
    buffer.close();
    tickProvider.fireTick();
    verify(mockCloseListener).onClose();
  }

  @Test
  public void testCloseListenerWaitForDecay() {
    animation.addCloseListener(mockCloseListener);

    buffer.setPixel(0, 0, pixel);
    buffer.nextFrame();
    buffer.close();
    verifyZeroInteractions(mockCloseListener);

    tickProvider.fireTick();
    tickProvider.fireTick();
    verifyZeroInteractions(mockCloseListener);

    tickProvider.fireTick();
    verify(mockCloseListener).onClose();
  }

  @Test
  public void testWriteAfterClose() {
    // Put pixel into decay frame so animation doesn't close immediately.
    buffer.setPixel(0, 0, pixel);
    buffer.nextFrame();
    buffer.close();

    tickProvider.fireTick();

    // Try to write after closing
    buffer.setPixel(0, 1, pixel);
    assertThat(animation.getPixel(0, 1)).isEqualTo(Pixel.EMPTY);
  }
}
