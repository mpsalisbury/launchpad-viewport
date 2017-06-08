package com.salisburyclan.lpviewport.device.midi.mk2;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.primitives.Bytes;
import com.salisburyclan.lpviewport.device.midi.mk2.LaunchpadMk2Constants.ButtonMapping;
import com.salisburyclan.lpviewport.device.midi.mk2.LaunchpadMk2Constants.ColorMapping;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class LaunchpadMk2ProtocolClientTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Receiver mockDevice;

  // Holds commands expected to be sent to device before verifying all at end of test.
  private List<byte[]> expectedCommands = new ArrayList<>();

  @Test
  public void testGetOverallExtent() {
    LaunchpadProtocolClient client = new LaunchpadMk2ProtocolClient(mockDevice);
    Range2 overallExtent = client.getOverallExtent();
    assertThat(overallExtent).isEqualTo(Range2.create(0, 0, 8, 8));
  }

  @Test
  public void testGetPadsExtent() {
    LaunchpadProtocolClient client = new LaunchpadMk2ProtocolClient(mockDevice);
    Range2 padsExtent = client.getPadsExtent();
    assertThat(padsExtent).isEqualTo(Range2.create(0, 0, 7, 7));
  }

  @Test
  public void testSetLight() throws Exception {
    // Try different buttons
    ColorMapping sampleColorMapping = LaunchpadMk2Constants.colorMappings[0];
    for (ButtonMapping buttonMapping : LaunchpadMk2Constants.buttonMappings) {
      testSetLight(
          buttonMapping.pos,
          buttonMapping.note,
          sampleColorMapping.color,
          sampleColorMapping.red,
          sampleColorMapping.green,
          sampleColorMapping.blue);
    }
    // Try different colors
    ButtonMapping sampleButtonMapping = LaunchpadMk2Constants.buttonMappings[0];
    for (ColorMapping colorMapping : LaunchpadMk2Constants.colorMappings) {
      testSetLight(
          sampleButtonMapping.pos,
          sampleButtonMapping.note,
          colorMapping.color,
          colorMapping.red,
          colorMapping.green,
          colorMapping.blue);
    }
    verifyExpectedCommands();
  }

  private void testSetLight(int xyPosition, int midiNote, int color, int red, int green, int blue)
      throws Exception {
    LaunchpadProtocolClient client = new LaunchpadMk2ProtocolClient(mockDevice);
    client.setLight(xyPosition, color);
    recordExpectedCommand(getSetLightCommand(midiNote, red, green, blue));
  }

  private byte[] getSetLightCommand(int midiNote, int red, int green, int blue) {
    ArrayList<Integer> command = new ArrayList<>();
    command.add(0xf0);
    command.add(0x00);
    command.add(0x20);
    command.add(0x29);
    command.add(0x02);
    command.add(0x18);
    command.add(0x0b);
    command.add(midiNote);
    command.add(red);
    command.add(green);
    command.add(blue);
    command.add(0xf7);
    return Bytes.toArray(command);
  }

  private void recordExpectedCommand(byte[] commandBytes) {
    expectedCommands.add(commandBytes);
  }

  private void verifyExpectedCommands() throws Exception {
    ArgumentCaptor<SysexMessage> sysexArgument = ArgumentCaptor.forClass(SysexMessage.class);
    verify(mockDevice, times(expectedCommands.size())).send(sysexArgument.capture(), eq(-1L));
    List<SysexMessage> observedCommands = sysexArgument.getAllValues();

    for (int i = 0; i < expectedCommands.size(); ++i) {
      assertThat(observedCommands.get(i).getMessage()).isEqualTo(expectedCommands.get(i));
    }
    expectedCommands.clear();
  }
}
