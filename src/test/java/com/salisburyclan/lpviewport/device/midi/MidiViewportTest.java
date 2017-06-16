package com.salisburyclan.lpviewport.device.midi;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class MidiViewportTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private LaunchpadProtocolClient mockClient;
  @Mock private MidiListener mockMidiListener;
  @Mock private Button2Listener mockButton2Listener;

  private MidiViewport viewport;

  private Range2 testExtent = Range2.create(0, 2, 10, 6);

  @Before
  public void setUp() {
    when(mockClient.getOverallExtent()).thenReturn(testExtent);
    viewport = new MidiViewport(mockClient, mockMidiListener);
  }

  @Test
  public void testExtent() throws Exception {
    assertThat(viewport.getExtent().getWidth()).isEqualTo(testExtent.getWidth());
    assertThat(viewport.getExtent().getHeight()).isEqualTo(testExtent.getHeight());
  }

  @Test
  public void testSetLight() throws Exception {
    int testX = 4;
    int testY = 2;
    Color testColor = Color.ORANGE;

    viewport.getLightLayer().setLight(testX, testY, testColor);

    verify(mockClient)
        .setLight(
            PositionCode.fromXY(testX, testY),
            ColorCode.fromRGB(testColor.getRed(), testColor.getGreen(), testColor.getBlue()));
  }

  @Test
  public void testAddListener() throws Exception {
    viewport.addListener(mockButton2Listener);
    verify(mockMidiListener).addListener(mockButton2Listener);
  }
}
