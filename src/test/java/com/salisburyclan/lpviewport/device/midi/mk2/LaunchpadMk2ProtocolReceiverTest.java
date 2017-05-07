package com.salisburyclan.launchpad.device.midi.mk2;

import com.salisburyclan.launchpad.device.midi.mk2.LaunchpadMk2Constants.ButtonMapping;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class LaunchpadMk2ProtocolReceiverTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private LaunchpadProtocolListener mockListener;

  @Test
  public void testButtonPress() throws Exception {
    for (ButtonMapping buttonMapping : LaunchpadMk2Constants.buttonMappings) {
      testButtonPress(buttonMapping.pos, buttonMapping.note);
    }
  }

  private void testButtonPress(int xyPosition, int midiNote) throws Exception {
    int noteOn = 100;
    int noteOff = 0;
    long timestamp = -1L;
    Receiver receiver = new LaunchpadMk2ProtocolReceiver(mockListener);
    receiver.send(new ShortMessage(ShortMessage.NOTE_ON, midiNote, noteOn), timestamp);
    receiver.send(new ShortMessage(ShortMessage.NOTE_ON, midiNote, noteOff), timestamp);

    verify(mockListener).onButtonPressed(xyPosition, timestamp);
    verify(mockListener).onButtonReleased(xyPosition, timestamp);
  }
}
