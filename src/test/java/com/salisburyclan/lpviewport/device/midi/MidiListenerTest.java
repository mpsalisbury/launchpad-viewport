package com.salisburyclan.lpviewport.device.midi;

import static org.mockito.Mockito.verify;

import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class MidiListenerTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private ViewportListener mockViewportListener;

  @Test
  public void testMessageForwarding() throws Exception {
    MidiListener listener = new MidiListener();
    listener.addListener(mockViewportListener);

    Point testPoint = Point.create(4, 2);

    listener.onButtonPressed(PositionCode.fromPoint(testPoint), -1L);
    listener.onButtonReleased(PositionCode.fromPoint(testPoint), -1L);

    verify(mockViewportListener).onButtonPressed(testPoint);
    verify(mockViewportListener).onButtonReleased(testPoint);
  }
}
