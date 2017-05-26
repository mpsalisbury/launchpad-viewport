package com.salisburyclan.lpviewport.device.midi.mk2;

import com.salisburyclan.lpviewport.device.midi.MidiDeviceSpec;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;
import javax.sound.midi.Receiver;

/** Launchpad MK2 device specs. */
public class LaunchpadMk2MidiDeviceSpec implements MidiDeviceSpec {
  public String getType() {
    return "mk2";
  }

  public String getSignature() {
    return "Launchpad MK2";
  }

  public LaunchpadProtocolClient newProtocolClient(Receiver receiver) {
    return new LaunchpadMk2ProtocolClient(receiver);
  }

  public Receiver newProtocolReceiver(LaunchpadProtocolListener listener) {
    return new LaunchpadMk2ProtocolReceiver(listener);
  }
}
