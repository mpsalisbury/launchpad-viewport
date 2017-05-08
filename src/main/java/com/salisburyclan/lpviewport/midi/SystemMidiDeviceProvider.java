package com.salisburyclan.lpviewport.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 * System implementation for Midi methods.
 */
public class SystemMidiDeviceProvider implements MidiDeviceProvider {
  public MidiDevice getMidiDevice(MidiDevice.Info info) throws MidiUnavailableException {
    return MidiSystem.getMidiDevice(info);
  }

  public MidiDevice.Info[] getMidiDeviceInfo() {
    return MidiSystem.getMidiDeviceInfo();
  }
}
