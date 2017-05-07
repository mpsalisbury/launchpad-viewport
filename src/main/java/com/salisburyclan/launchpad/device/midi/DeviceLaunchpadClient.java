package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.LaunchpadException;
import com.salisburyclan.launchpad.api.Viewport;
import com.salisburyclan.launchpad.api.ViewExtent;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * Represents a physical MIDI Launchpad device.
 */
public class DeviceLaunchpadClient implements LaunchpadClient {

  private final Viewport viewport;
  private DeviceResources resources;

  public DeviceLaunchpadClient(DeviceResources resources) throws MidiUnavailableException {
    if (resources == null) {
      throw new IllegalArgumentException("Resources must not be null");
    }
    this.resources = resources;

    this.viewport = new DeviceViewport(resources.getClient(), resources.getListener());
//    ViewExtent padExtent = client.getPadsExtent();
  }

  @Override
  public String getType() {
    return resources.getType();
  }

  @Override
  public Viewport getViewport() {
    return viewport;
  }

  @Override
  public void close() {
    resources.close();
  }
}
