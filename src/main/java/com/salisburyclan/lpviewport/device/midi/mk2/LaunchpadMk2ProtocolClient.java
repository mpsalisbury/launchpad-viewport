package com.salisburyclan.lpviewport.device.midi.mk2;

import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.device.midi.ColorCode;
import com.salisburyclan.lpviewport.device.midi.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.midi.SysexBuilder;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import java.util.Arrays;

public class LaunchpadMk2ProtocolClient implements LaunchpadProtocolClient {

  /** The Launchpad's Receiver, to which commands are sent. */
  private final Receiver receiver;
  private final LaunchpadDevice device = new LaunchpadMk2Device();

  /**
   * @param receiver The Launchpad's MIDI Receiver. Must not be null.
   */
  public LaunchpadMk2ProtocolClient(Receiver receiver) {
    if (receiver == null) {
        throw new IllegalArgumentException("Receiver must not be null.");
    }
    // TODO(mike): Ensure that receiver is in fact a LaunchpadMk2
    this.receiver = receiver;
  }

  @Override
  public ViewExtent getOverallExtent() {
    return device.getOverallExtent();
  }

  @Override
  public ViewExtent getPadsExtent() {
    return device.getPadsExtent();
  }

  @Override
  public void setLight(int pos, int color) {
    byte ledIndex = device.posToIndex(pos);
    byte red = ColorCode.getRed(color);
    byte green = ColorCode.getGreen(color);
    byte blue = ColorCode.getBlue(color);
    byte[] command = {0x0b, ledIndex, red, green, blue};
    send(command);
  }

  /*
  @Override
  public void setLights(int extent, int[] colors) {
    int xLow = ViewExtent.getXLow(extent);
    int xHigh = ViewExtent.getXHigh(extent);
    int yLow = ViewExtent.getYLow(extent);
    int yHigh = ViewExtent.getYHigh(extent);
    int size = (xHigh - xLow + 1) * (yHigh - yLow + 1);
    if (size != colors.length) {
      throw new IllegalArgumentException("Invalid colors length, expected " + size +
          ", received " + colors.length);
    }
    int i = 0;
    SysexBuilder builder = newSysexBuilder();
    builder.add((byte)0x0b);
    for (int x = xLow; x <= xHigh; ++x) {
      for (int y = yLow; y <= yHigh; ++y) {
        int color = colors[i];
        builder.add(device.posToIndex(x, y),
            Color.getRed(color), Color.getGreen(color), Color.getBlue(color));
      }
    }
    send(builder.build());
  }

  @Override
  public void setLights(long[] poscolors) {
    if (poscolors.length > 80) {
      throw new IllegalArgumentException("Too many poscolors specified: " +
          poscolors.length + ", max 80");
    }
    SysexBuilder builder = newSysexBuilder();
    builder.add((byte)0x0b);
    for (long poscolor: poscolors) {
      builder.add(device.posToIndex(PositionColor.getX(poscolor), PositionColor.getY(poscolor)),
          PositionColor.getRed(poscolor),
          PositionColor.getGreen(poscolor),
          PositionColor.getBlue(poscolor));
    }
    send(builder.build());
  }

  @Override
  public void setLights(int extent, int color) {
    int xLow = ViewExtent.getXLow(extent);
    int xHigh = ViewExtent.getXHigh(extent);
    int yLow = ViewExtent.getYLow(extent);
    int yHigh = ViewExtent.getYHigh(extent);
    byte red = Color.getRed(color);
    byte green = Color.getGreen(color);
    byte blue = Color.getBlue(color);

    SysexBuilder builder = newSysexBuilder();
    builder.add((byte)0x0b);
    for (int x = xLow; x <= xHigh; ++x) {
      for (int y = yLow; y <= yHigh; ++y) {
        builder.add(device.posToIndex(x, y), red, green, blue);
      }
    }
    send(builder.build());
  }

  @Override
  public void clearLights() {
    byte[] command = {0x0e, 0x00};
    send(command);
  }
  */

  private SysexBuilder newSysexBuilder() {
    return new SysexBuilder(device.getSysexPreamble());
  }

  /** Sends the given message to the Launchpad. */
  private void send(SysexMessage message) {
    //System.out.println("sending: " + Arrays.toString(message.getMessage()));
    receiver.send(message, -1);

    /*
    try {
      ShortMessage m1 = new ShortMessage(ShortMessage.NOTE_ON, 81, 45);
      System.out.println("m1: " + Arrays.toString(m1.getMessage()));
      receiver.send(m1, -1);
    } catch (InvalidMidiDataException e) {
      System.err.println(e.getMessage());
    }
    */
  }

  /** Sends the given sysex command to the Launchpad. */
  private void send(byte[] command) {
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command));

    /*
    byte[] command1 = {0x7e, 0x7f, 0x06, 0x01};
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command1));
    byte[] command1 = {10, 84, 45};
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command1));
    byte[] command2= {13, 2, 45};
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command2));
    byte[] command3 = {12, 2, 45};
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command3));
    */

  }
}
