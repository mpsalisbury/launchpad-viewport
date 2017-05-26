package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public class MidiResources {

  private final MidiDevice inputDevice;
  private final MidiDevice outputDevice;
  private final String type;
  private final LaunchpadProtocolClient protocolClient;
  private final MidiListener midiListener;

  public MidiResources(MidiDeviceSpec spec, MidiDevice inputDevice, MidiDevice outputDevice)
      throws MidiUnavailableException {
    // TODO: Require inputDevice has transmitters and outputDevice has receivers.
    this.inputDevice = inputDevice;
    if (!inputDevice.isOpen()) {
      inputDevice.open();
    }
    this.outputDevice = outputDevice;
    if (!outputDevice.isOpen()) {
      outputDevice.open();
    }
    this.type = spec.getType();
    this.protocolClient = spec.newProtocolClient(outputDevice.getReceiver());
    this.midiListener = new MidiListener();
    inputDevice.getTransmitter().setReceiver(spec.newProtocolReceiver(midiListener));
  }

  public String getType() {
    return type;
  }

  public LaunchpadProtocolClient getClient() {
    return protocolClient;
  }

  public MidiListener getListener() {
    return midiListener;
  }

  public void close() {
    if (inputDevice.isOpen()) {
      inputDevice.close();
    }
    if (outputDevice.isOpen()) {
      outputDevice.close();
    }
  }
}
