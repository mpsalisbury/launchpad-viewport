package com.salisburyclan.lpviewport.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;

/** System implementation for Midi methods. */
public class SystemMidiDeviceProvider implements MidiDeviceProvider {
  public MidiDevice getMidiDevice(MidiDevice.Info info) throws MidiUnavailableException {
    return MidiSystem.getMidiDevice(info);
  }

  public MidiDevice.Info[] getMidiDeviceInfo() {
    // CoreMidiDeviceProvider will provide proper Midi devices on OSX but leave
    // all MidiSystem-provided devices alone on other platforms.
    return CoreMidiDeviceProvider.getMidiDeviceInfo();
  }
}
