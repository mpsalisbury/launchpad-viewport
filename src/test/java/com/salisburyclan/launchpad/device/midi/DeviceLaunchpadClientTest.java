package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.Color;
import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.Viewport;
import com.salisburyclan.launchpad.api.ViewportListener;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.ViewExtent;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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
public class DeviceLaunchpadClientTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private DeviceResources mockResources;
  @Mock private LaunchpadProtocolClient mockClient;
  @Mock private DeviceListener mockDeviceListener;
  @Mock private ViewportListener mockViewportListener;

  @Test
  public void testGetOverallViewport() throws Exception {
    ViewExtent testExtent = new ViewExtent(0,10,2,6);

    when(mockResources.getClient()).thenReturn(mockClient);
    when(mockResources.getListener()).thenReturn(mockDeviceListener);
    when(mockClient.getOverallExtent()).thenReturn(testExtent);

    LaunchpadClient client = new DeviceLaunchpadClient(mockResources);
    Viewport viewport = client.getViewport();

    assertThat(viewport.getWidth()).isEqualTo(testExtent.getWidth());
    assertThat(viewport.getHeight()).isEqualTo(testExtent.getHeight());

    int testX = 4;
    int testY = 2;
    Color testColor = Color.ORANGE;
    viewport.setLight(testX, testY, testColor);
    verify(mockClient).setLight(PositionCode.fromXY(testX, testY),
        ColorCode.fromRGB(testColor.getRed(), testColor.getGreen(), testColor.getBlue()));

    viewport.addListener(mockViewportListener);
    verify(mockDeviceListener).addListener(mockViewportListener);
  }

}
