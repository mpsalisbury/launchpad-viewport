package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;

import javax.sound.midi.Receiver;

/** Describes a Launchpad device. */
public interface MidiDeviceSpec {
  String getType();
  String getSignature();
  LaunchpadProtocolClient newProtocolClient(Receiver receiver);
  Receiver newProtocolReceiver(LaunchpadProtocolListener listener);
}

