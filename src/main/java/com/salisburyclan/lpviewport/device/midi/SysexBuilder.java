package com.salisburyclan.lpviewport.device.midi;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

/** Builds SysexMessages for sending to the Launchpad. */
public class SysexBuilder {

  private byte[] preamble;

  // Holds the command bytes that will be included in the message.
  // TODO(mike): more efficient mechanism
  private ArrayList<Byte> command = new ArrayList<>();

  // @param preamble the starting bytes of the command, not including the initial 0xf0.
  public SysexBuilder(byte[] preamble) {
    this.preamble = preamble;
  }

  // Adds the given bytes to the command.
  public void add(byte... bs) {
    for (byte b : bs) {
      command.add(b);
    }
  }

  // Composes the given command bytes into a new SysexMessage.
  public SysexMessage build() {
    byte[] commandBytes = new byte[command.size()];
    int i = 0;
    for (Byte b : command) {
      commandBytes[i] = b;
      ++i;
    }
    return newMessage(preamble, commandBytes);
  }

  // Returns a new SysexMessage given the preamble and command
  public static SysexMessage newMessage(byte[] preamble, byte[] command) {
    ByteBuffer buffer = ByteBuffer.allocate(2 + preamble.length + command.length);
    buffer.put((byte) 0xf0);
    buffer.put(preamble);
    buffer.put(command);
    buffer.put((byte) 0xf7);

    try {
      return new SysexMessage(buffer.array(), buffer.array().length);
    } catch (InvalidMidiDataException e) {
      throw new IllegalStateException("Unexpected error", e);
    }
  }
}
