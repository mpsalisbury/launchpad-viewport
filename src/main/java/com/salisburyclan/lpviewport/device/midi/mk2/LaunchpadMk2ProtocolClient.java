package com.salisburyclan.lpviewport.device.midi.mk2;

import com.salisburyclan.lpviewport.device.midi.ColorCode;
import com.salisburyclan.lpviewport.device.midi.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.midi.SysexBuilder;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

public class LaunchpadMk2ProtocolClient implements LaunchpadProtocolClient {

  // The Launchpad's Receiver, to which commands are sent.
  private final Receiver receiver;

  private final LaunchpadDevice device = new LaunchpadMk2Device();

  // @param receiver The Launchpad's MIDI Receiver. Must not be null.
  public LaunchpadMk2ProtocolClient(Receiver receiver) {
    if (receiver == null) {
      throw new IllegalArgumentException("Receiver must not be null.");
    }
    // TODO(mike): Ensure that receiver is in fact a LaunchpadMk2
    this.receiver = receiver;
  }

  @Override
  public Range2 getOverallExtent() {
    return device.getOverallExtent();
  }

  @Override
  public Range2 getPadsExtent() {
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
  */

  @Override
  public void setLights(Range2 extent, int color) {
    byte red = ColorCode.getRed(color);
    byte green = ColorCode.getGreen(color);
    byte blue = ColorCode.getBlue(color);

    SysexBuilder builder = newSysexBuilder();
    builder.add((byte) 0x0b);
    extent.forEach((x, y) -> builder.add(device.posToIndex(x, y), red, green, blue));
    send(builder.build());
  }

  private SysexBuilder newSysexBuilder() {
    return new SysexBuilder(device.getSysexPreamble());
  }

  // Sends the given message to the Launchpad.
  private void send(SysexMessage message) {
    receiver.send(message, -1);
  }

  // Sends the given sysex command to the Launchpad.
  private void send(byte[] command) {
    send(SysexBuilder.newMessage(device.getSysexPreamble(), command));
  }
}
