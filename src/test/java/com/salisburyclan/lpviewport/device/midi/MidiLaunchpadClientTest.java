package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
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
public class MidiLaunchpadClientTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private MidiResources mockResources;
  @Mock private LaunchpadProtocolClient mockClient;
  @Mock private MidiListener mockMidiListener;
  @Mock private ViewportListener mockViewportListener;

  @Test
  public void testGetOverallViewport() throws Exception {
    ViewExtent testExtent = new ViewExtent(0,2,10,6);

    when(mockResources.getClient()).thenReturn(mockClient);
    when(mockResources.getListener()).thenReturn(mockMidiListener);
    when(mockClient.getOverallExtent()).thenReturn(testExtent);

    LaunchpadClient client = new MidiLaunchpadClient(mockResources);
    Viewport viewport = client.getViewport();

    assertThat(viewport.getExtent().getWidth()).isEqualTo(testExtent.getWidth());
    assertThat(viewport.getExtent().getHeight()).isEqualTo(testExtent.getHeight());

    int testX = 4;
    int testY = 2;
    Color testColor = Color.ORANGE;
    viewport.setLight(testX, testY, testColor);
    verify(mockClient).setLight(PositionCode.fromXY(testX, testY),
        ColorCode.fromRGB(testColor.getRed(), testColor.getGreen(), testColor.getBlue()));

    viewport.addListener(mockViewportListener);
    verify(mockMidiListener).addListener(mockViewportListener);
  }

}
