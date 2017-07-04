package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.LaunchpadDeviceProvider;
import com.salisburyclan.lpviewport.midi.MidiDeviceProvider;
import com.salisburyclan.lpviewport.midi.SystemMidiDeviceProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/** Provides Devices for Midi devices. */
public class MidiLaunchpadDeviceProvider implements LaunchpadDeviceProvider {
  private static final Logger logger =
      Logger.getLogger(MidiLaunchpadDeviceProvider.class.getName());

  // Identifier for OSX
  private static final String OSX_ID = "macosx";

  // Label added to Midi device names for OSX CoreMIDI4J device wrappers.
  private static final String CORE_MIDI_4J_NAME_TAG = "CoreMIDI4J";

  // Provides access to midi devices.
  private MidiDeviceProvider deviceProvider;
  private MidiDeviceSpecProvider specProvider;

  public MidiLaunchpadDeviceProvider() {
    this.deviceProvider = new SystemMidiDeviceProvider();
    this.specProvider = new ProdMidiDeviceSpecProvider();
  }

  public MidiLaunchpadDeviceProvider(
      MidiDeviceProvider deviceProvider, MidiDeviceSpecProvider specProvider) {
    this.deviceProvider = deviceProvider;
    this.specProvider = specProvider;
  }

  @Override
  public boolean supportsDeviceSpec(String deviceSpec) {
    return specProvider
        .getSpecs()
        .stream()
        .map(MidiDeviceSpec::getType)
        .anyMatch(type -> type.equals(deviceSpec));
  }

  @Override
  public Set<String> getAvailableTypes() {
    return specProvider
        .getSpecs()
        .stream()
        .map(MidiDeviceSpec::getType)
        .collect(Collectors.toSet());
  }

  @Override
  public List<LaunchpadDevice> getDevices(String deviceSpec) {
    DeviceFetcher fetcher = new DeviceFetcher();
    fetcher.collectDevices(deviceSpec);
    return fetcher.getFoundDevices();
  }

  private class DeviceFetcher {
    private List<LaunchpadDevice> devices;
    private Map<String, MidiDevice> unmatchedInputs;
    private Map<String, MidiDevice> unmatchedOutputs;

    public DeviceFetcher() {
      devices = new ArrayList<>();
      unmatchedInputs = new HashMap<>();
      unmatchedOutputs = new HashMap<>();
    }

    public void collectDevices(String deviceSpec) {
      AtomicBoolean foundValidType = new AtomicBoolean(false);
      specProvider
          .getSpecs()
          .forEach(
              spec -> {
                Arrays.stream(deviceProvider.getMidiDeviceInfo())
                    .forEach(
                        info -> {
                          logger.info(
                              String.format(
                                  "Got midi info %s / %s", info.getName(), info.getDescription()));
                          if (deviceSpec.equals(spec.getType())) {
                            foundValidType.set(true);
                            if (isDeviceCompatibleWithPlatform(info)
                                && deviceMatchesSignature(spec.getSignature(), info)) {
                              addDevice(spec, info);
                            }
                          }
                        });
              });
      if (devices.isEmpty()) {
        if (foundValidType.get()) {
          logger.warning("Requested device(s) not connected");
        } else {
          logger.warning("Invalid TypeSpec requested");
        }
      }
    }

    private List<LaunchpadDevice> getFoundDevices() {
      return devices;
    }

    private boolean deviceMatchesSignature(String signature, MidiDevice.Info info) {
      return info.getDescription().contains(signature);
    }

    private boolean isDeviceCompatibleWithPlatform(MidiDevice.Info info) {
      if (isPlatformOsx()) {
        // On OSX the Java's javax.sound.midi.MidiDevice doesn't send SysEx messages
        // to the device.  To work around this, we include the CoreMidi4J library,
        // which wraps the CoreMidi connections to the devices with a Java MidiDevice,
        // prefixing the name with "CoreMIDI4J".  Here we identify only devices with
        // that tag as being available on osx systems.
        return info.getName().contains(CORE_MIDI_4J_NAME_TAG);
      }
      return true;
    }

    private boolean isPlatformOsx() {
      final String os = System.getProperty("os.name").toLowerCase().replace(" ", "");
      return OSX_ID.equals(os);
    }

    /** Adds the specified device as a client. Returns true upon success. */
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
        logger.warning(
            String.format(
                "Unable to access midi device %s\n%s", info.getDescription(), e.getMessage()));
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
      MidiResources resources = new MidiResources(spec, inputDevice, outputDevice);
      devices.add(new MidiLaunchpadDevice(resources));
    }
  }
}
