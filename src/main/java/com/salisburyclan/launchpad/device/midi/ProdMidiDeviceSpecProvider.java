package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.device.midi.mk2.LaunchpadMk2MidiDeviceSpec;
import java.util.ArrayList;
import java.util.List;

public class ProdMidiDeviceSpecProvider implements MidiDeviceSpecProvider {
  private static List<MidiDeviceSpec> specs = null;

  public ProdMidiDeviceSpecProvider() {
    specs = new ArrayList<>();
    specs.add(new LaunchpadMk2MidiDeviceSpec());
  }

  public List<MidiDeviceSpec> getSpecs() {
    return specs;
  }
}
