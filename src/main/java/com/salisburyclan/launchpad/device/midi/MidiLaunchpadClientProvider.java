package com.salisburyclan.launchpad.device.midi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.LaunchpadClientProvider;
import com.salisburyclan.launchpad.midi.MidiDeviceProvider;
import com.salisburyclan.launchpad.midi.SystemMidiDeviceProvider;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/** Provides LaunchpadClients for Midi devices. */
public class MidiLaunchpadClientProvider implements LaunchpadClientProvider {

  // Provides access to midi devices.
  private MidiDeviceProvider deviceProvider;
  private MidiDeviceSpecProvider specProvider;

  public MidiLaunchpadClientProvider() {
    this.deviceProvider = new SystemMidiDeviceProvider();
    this.specProvider = new ProdMidiDeviceSpecProvider();
  }

  public MidiLaunchpadClientProvider(MidiDeviceProvider deviceProvider,
      MidiDeviceSpecProvider specProvider) {
    this.deviceProvider = deviceProvider;
    this.specProvider = specProvider;
  }

  @Override
  public Set<String> getAvailableTypes() {
    return specProvider.getSpecs().stream()
      .map(MidiDeviceSpec::getType)
      .collect(Collectors.toSet());
  }

  @Override
  public List<LaunchpadClient> getLaunchpadClients(Set<String> typeSpecs) {
    DeviceFetcher fetcher = new DeviceFetcher();
    fetcher.collectDevices(typeSpecs);
    return fetcher.getFoundClients();
  }

  private class DeviceFetcher {
    private List<LaunchpadClient> clients;
    private Map<String, MidiDevice> unmatchedInputs;
    private Map<String, MidiDevice> unmatchedOutputs;

    public DeviceFetcher() {
      clients = new ArrayList<>();
      unmatchedInputs = new HashMap<>();
      unmatchedOutputs = new HashMap<>();
    }

    public void collectDevices(Set<String> typeSpecs) {
      AtomicBoolean foundValidType = new AtomicBoolean(false);
      specProvider.getSpecs().forEach((spec)->{
        Arrays.stream(deviceProvider.getMidiDeviceInfo()).forEach((info)->{
          System.out.println(String.format("Got midi info %s / %s", info.getName(), info.getDescription()));
          if (typeSpecs.contains(spec.getType())) {
            foundValidType.set(true);
            if (deviceMatchesSignature(spec.getSignature(), info)) {
              addDevice(spec, info);
            }
          }
        });
      });
      if (clients.isEmpty()) {
        if (foundValidType.get()) {
          System.err.println("Requested device(s) not connected");
        } else {
          System.err.println("Invalid TypeSpec requested");
        }
      }
    }

    private List<LaunchpadClient> getFoundClients() {
      return clients;
    }

    private boolean deviceMatchesSignature(String signature, MidiDevice.Info info) {
      return info.getDescription().contains(signature);
    }

    /**
     * Adds the specified device as a client.
     * Returns true upon success.
     */
    private boolean addDevice(MidiDeviceSpec spec, MidiDevice.Info info) {
      try {
        MidiDevice device = deviceProvider.getMidiDevice(info);
        if (device.getMaxTransmitters() != 0) {
          addInputDevice(spec, device);
          return true;
        }
        if (device.getMaxReceivers() != 0) {
          addOutputDevice(spec, device);
          return true;
        }
        device.close();
        return false;
      } catch (MidiUnavailableException e) {
        System.err.println(
            String.format("Unable to access midi device %s\n%s",
                info.getDescription(), e.getMessage()));
        return false;
      }
    }

    private void addInputDevice(MidiDeviceSpec spec, MidiDevice inputDevice)
        throws MidiUnavailableException {
      String type = spec.getType();
      if (unmatchedOutputs.containsKey(type)) {
        MidiDevice outputDevice = unmatchedOutputs.remove(type);
        addClient(spec, inputDevice, outputDevice);
      } else {
        unmatchedInputs.put(type, inputDevice);
      }
    }

    private void addOutputDevice(MidiDeviceSpec spec, MidiDevice outputDevice)
        throws MidiUnavailableException {
      String type = spec.getType();
      if (unmatchedInputs.containsKey(type)) {
        MidiDevice inputDevice = unmatchedInputs.remove(type);
        addClient(spec, inputDevice, outputDevice);
      } else {
        unmatchedOutputs.put(type, outputDevice);
      }
    }

    private void addClient(MidiDeviceSpec spec, MidiDevice inputDevice, MidiDevice outputDevice)
        throws MidiUnavailableException {
      DeviceResources resources = new DeviceResources(spec, inputDevice, outputDevice);
      clients.add(new DeviceLaunchpadClient(resources));
    }
  }
}
