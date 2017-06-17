package com.salisburyclan.lpviewport.device.midi;

import static org.mockito.Mockito.verify;

import com.salisburyclan.lpviewport.api.Button2Listener;
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
  @Mock private Button2Listener mockButton2Listener;

  @Test
  public void testMessageForwarding() throws Exception {
    MidiListener listener = new MidiListener();
    listener.addListener(mockButton2Listener);

    Point testPoint = Point.create(4, 2);

    listener.onButtonPressed(PositionCode.fromPoint(testPoint), -1L);
    listener.onButtonReleased(PositionCode.fromPoint(testPoint), -1L);

    verify(mockButton2Listener).onButtonPressed(testPoint);
    verify(mockButton2Listener).onButtonReleased(testPoint);
  }
}
