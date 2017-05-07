package com.salisburyclan.launchpad.device.midi;

import javax.sound.midi.SysexMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class SysexBuilderTest {

  @Test
  public void testNewMessage() {
    byte[] preamble = {0x00, 0x01, 0x02, 0x03};
    byte[] command = {0x04, 0x05, 0x06, 0x07};
    byte[] expectedMessage = {(byte)0xf0, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, (byte)0xf7};
    SysexMessage message = SysexBuilder.newMessage(preamble, command);
    assertThat(message.getMessage()).isEqualTo(expectedMessage);
  }
}
