package com.salisburyclan.launchpad.device.midi;

import java.util.List;

public interface MidiDeviceSpecProvider {
  List<MidiDeviceSpec> getSpecs();
}
