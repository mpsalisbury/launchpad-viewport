package com.salisburyclan.lpviewport.device.midi;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class MidiLaunchpadDeviceTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private MidiResources mockResources;
  @Mock private LaunchpadProtocolClient mockClient;
  @Mock private MidiListener mockMidiListener;
  @Mock private ViewportListener mockViewportListener;

  @Test
  public void testGetOverallViewport() throws Exception {
    Range2 testExtent = Range2.create(0, 2, 10, 6);

    when(mockResources.getClient()).thenReturn(mockClient);
    when(mockResources.getListener()).thenReturn(mockMidiListener);
    when(mockClient.getOverallExtent()).thenReturn(testExtent);

    LaunchpadDevice device = new MidiLaunchpadDevice(mockResources);
    Viewport viewport = device.getViewport();

    assertThat(viewport.getExtent().getWidth()).isEqualTo(testExtent.getWidth());
    assertThat(viewport.getExtent().getHeight()).isEqualTo(testExtent.getHeight());

    int testX = 4;
    int testY = 2;
    Color testColor = Color.ORANGE;
    viewport.setLight(testX, testY, testColor);
    verify(mockClient)
        .setLight(
            PositionCode.fromXY(testX, testY),
            ColorCode.fromRGB(testColor.getRed(), testColor.getGreen(), testColor.getBlue()));

    viewport.addListener(mockViewportListener);
    verify(mockMidiListener).addListener(mockViewportListener);
  }
}
