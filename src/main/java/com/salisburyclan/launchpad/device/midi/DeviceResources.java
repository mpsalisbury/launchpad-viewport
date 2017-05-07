package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.LaunchpadClient;

import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class DeviceResources {

  private final MidiDevice inputDevice;
  private final MidiDevice outputDevice;
  private final String type;
  private final LaunchpadProtocolClient protocolClient;
  private final DeviceListener deviceListener;

  public DeviceResources(MidiDeviceSpec spec,
      MidiDevice inputDevice, MidiDevice outputDevice) throws MidiUnavailableException {
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
    this.deviceListener = new DeviceListener();
    inputDevice.getTransmitter().setReceiver(spec.newProtocolReceiver(deviceListener));

    System.out.println("Receivers: " + outputDevice.getReceivers().size());
    System.out.println("Transmitters: " + inputDevice.getTransmitters().size());
  }

  public String getType() {
    return type;
  }

  public LaunchpadProtocolClient getClient() {
    return protocolClient;
  }

  public DeviceListener getListener() {
    return deviceListener;
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
