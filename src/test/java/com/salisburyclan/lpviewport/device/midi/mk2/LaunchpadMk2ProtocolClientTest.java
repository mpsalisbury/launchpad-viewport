package com.salisburyclan.launchpad.device.midi.mk2;

import com.google.common.primitives.Bytes;
import com.salisburyclan.launchpad.device.midi.mk2.LaunchpadMk2Constants.ButtonMapping;
import com.salisburyclan.launchpad.device.midi.mk2.LaunchpadMk2Constants.ColorMapping;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.ViewExtent;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class LaunchpadMk2ProtocolClientTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Receiver mockDevice;

  // Holds commands expected to be sent to device before verifying all at end of test.
  private List<byte[]> expectedCommands = new ArrayList<>();

  @Test
  public void testGetOverallExtent() {
    LaunchpadProtocolClient client = new LaunchpadMk2ProtocolClient(mockDevice);
    ViewExtent overallExtent = client.getOverallExtent();
    assertThat(overallExtent.getXLow()).isEqualTo(0);
    assertThat(overallExtent.getYLow()).isEqualTo(0);
    assertThat(overallExtent.getXHigh()).isEqualTo(8);
    assertThat(overallExtent.getYHigh()).isEqualTo(8);
  }

  @Test
  public void testGetPadsExtent() {
    LaunchpadProtocolClient client = new LaunchpadMk2ProtocolClient(mockDevice);
    ViewExtent padsExtent = client.getPadsExtent();
    assertThat(padsExtent.getXLow()).isEqualTo(0);
    assertThat(padsExtent.getYLow()).isEqualTo(0);
    assertThat(padsExtent.getXHigh()).isEqualTo(7);
    assertThat(padsExtent.getYHigh()).isEqualTo(7);
  }

  @Test
  public void testSetLight() throws Exception {
    // Try different buttons
    ColorMapping sampleColorMapping = LaunchpadMk2Constants.colorMappings[0];
    for (ButtonMapping buttonMapping : LaunchpadMk2Constants.buttonMappings) {
      testSetLight(buttonMapping.pos, buttonMapping.note,
          sampleColorMapping.color, sampleColorMapping.red,
	  sampleColorMapping.green, sampleColorMapping.blue);
    }
    // Try different colors
    ButtonMapping sampleButtonMapping = LaunchpadMk2Constants.buttonMappings[0];
    for (ColorMapping colorMapping : LaunchpadMk2Constants.colorMappings) {
      testSetLight(sampleButtonMapping.pos, sampleButtonMapping.note,
          colorMapping.color, colorMapping.red, colorMapping.green, colorMapping.blue);
    }
    verifyExpectedCommands();
  }

  private void testSetLight(int xyPosition, int midiNote, int color, int red, int green, int blue) throws Exception {
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
    verify(mockDevice, times(expectedCommands.size()))
        .send(sysexArgument.capture(), eq(-1L));
    List<SysexMessage> observedCommands = sysexArgument.getAllValues();

    for (int i = 0; i < expectedCommands.size(); ++i) {
      assertThat(observedCommands.get(i).getMessage())
        .isEqualTo(expectedCommands.get(i));
    }
    expectedCommands.clear();
  }

}
