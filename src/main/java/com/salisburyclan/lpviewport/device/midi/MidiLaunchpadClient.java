package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.LaunchpadException;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.device.ViewportAdapter;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * Represents a physical MIDI Launchpad device.
 */
public class MidiLaunchpadClient implements LaunchpadClient {

  private final Viewport viewport;
  private MidiResources resources;

  public MidiLaunchpadClient(MidiResources resources) throws MidiUnavailableException {
    if (resources == null) {
      throw new IllegalArgumentException("Resources must not be null");
    }
    this.resources = resources;

    this.viewport = new ViewportAdapter(
        new MidiViewport(resources.getClient(), resources.getListener()));
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
