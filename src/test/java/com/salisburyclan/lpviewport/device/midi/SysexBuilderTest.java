package com.salisburyclan.lpviewport.device.midi;

import static com.google.common.truth.Truth.assertThat;

import javax.sound.midi.SysexMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SysexBuilderTest {

  @Test
  public void testBuild() {
    byte[] preamble = {0x00, 0x01, 0x02};
    SysexBuilder builder = new SysexBuilder(preamble);
    builder.add((byte) 0x03);
    builder.add((byte) 0x04, (byte) 0x05);
    SysexMessage message = builder.build();

    byte[] expectedMessage = {(byte) 0xf0, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, (byte) 0xf7};
    assertThat(message.getMessage()).isEqualTo(expectedMessage);
  }

  @Test
  public void testNewMessage() {
    byte[] preamble = {0x00, 0x01, 0x02, 0x03};
    byte[] command = {0x04, 0x05, 0x06, 0x07};
    byte[] expectedMessage = {
      (byte) 0xf0, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, (byte) 0xf7
    };
    SysexMessage message = SysexBuilder.newMessage(preamble, command);
    assertThat(message.getMessage()).isEqualTo(expectedMessage);
  }
}
