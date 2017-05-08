package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.ViewportListener;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class DeviceListenerTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private ViewportListener mockViewportListener;

  @Test
  public void testMessageForwarding() throws Exception {
    DeviceListener listener = new DeviceListener();
    listener.addListener(mockViewportListener);

    int testX = 4;
    int testY = 2;

    listener.onButtonPressed(PositionCode.fromXY(testX, testY), -1L);
    listener.onButtonReleased(PositionCode.fromXY(testX, testY), -1L);

    verify(mockViewportListener).onButtonPressed(testX, testY);
    verify(mockViewportListener).onButtonReleased(testX, testY);
  }

}
