package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;

import javax.sound.midi.Receiver;

/** Describes a Launchpad device. */
public interface MidiDeviceSpec {
  String getType();
  String getSignature();
  LaunchpadProtocolClient newProtocolClient(Receiver receiver);
  Receiver newProtocolReceiver(LaunchpadProtocolListener listener);
}

