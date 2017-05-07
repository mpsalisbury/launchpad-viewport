package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.Color;
import com.salisburyclan.launchpad.api.ViewportListener;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.ViewExtent;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DeviceViewportTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private LaunchpadProtocolClient mockClient;
  @Mock private DeviceListener mockDeviceListener;
  @Mock private ViewportListener mockViewportListener;

  private DeviceViewport viewport;

  private ViewExtent testExtent = new ViewExtent(0,10,2,6);

  @Before
  public void setUp() {
    when(mockClient.getOverallExtent()).thenReturn(testExtent);
    viewport = new DeviceViewport(mockClient, mockDeviceListener);
  }

  @Test
  public void testExtent() throws Exception {
    assertThat(viewport.getWidth()).isEqualTo(testExtent.getWidth());
    assertThat(viewport.getHeight()).isEqualTo(testExtent.getHeight());
  }

  @Test
  public void testSetLight() throws Exception {
    int testX = 4;
    int testY = 2;
    Color testColor = Color.ORANGE;

    viewport.setLight(testX, testY, testColor);

    verify(mockClient).setLight(PositionCode.fromXY(testX, testY),
        ColorCode.fromRGB(testColor.getRed(), testColor.getGreen(), testColor.getBlue()));
  }

  @Test
  public void testAddListener() throws Exception {
    viewport.addListener(mockViewportListener);
    verify(mockDeviceListener).addListener(mockViewportListener);
  }
}
