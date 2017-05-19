package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LaunchpadException;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;

import javax.sound.midi.MidiUnavailableException;

/**
 * Represents a physical MIDI Launchpad device.
 */
public class MidiLaunchpadDevice implements LaunchpadDevice {

  private final Viewport viewport;
  private MidiResources resources;

  public MidiLaunchpadDevice(MidiResources resources) throws MidiUnavailableException {
    if (resources == null) {
      throw new IllegalArgumentException("Resources must not be null");
    }
    this.resources = resources;

    this.viewport = new MidiViewport(resources.getClient(), resources.getListener());
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
