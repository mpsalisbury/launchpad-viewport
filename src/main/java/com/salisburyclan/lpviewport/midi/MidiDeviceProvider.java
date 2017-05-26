package com.salisburyclan.lpviewport.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/** Interface for providing or mocking Midi system services. */
public interface MidiDeviceProvider {
  MidiDevice getMidiDevice(MidiDevice.Info info) throws MidiUnavailableException;

  MidiDevice.Info[] getMidiDeviceInfo();
}
