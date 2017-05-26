package com.salisburyclan.lpviewport.device.midi;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.midi.MidiDeviceProvider;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

// TODO: Cleanup entire class
//
@RunWith(JUnit4.class)
public class MidiLaunchpadDeviceProviderTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Receiver mockReceiver;
  @Mock private Transmitter mockTransmitter;
  @Mock private LaunchpadProtocolClient mockProtocolClient;
  @Mock private Receiver mockProtocolReceiver;

  private MidiLaunchpadDeviceProvider deviceProvider;

  @Before
  public void setUp() {
    deviceProvider =
        new MidiLaunchpadDeviceProvider(
            new FakeDeviceProvider(ImmutableList.of("A", "C", "D", "E", "E", "E")),
            new FakeSpecProvider(ImmutableList.of("A", "B", "C", "E")));

    when(mockProtocolClient.getOverallExtent()).thenReturn(new ViewExtent(0, 0, 10, 10));
  }

  @Test
  public void testGetDevicesOneType() throws Exception {
    List<LaunchpadDevice> devices = deviceProvider.getDevices("A");
    assertThat(devices.size()).isEqualTo(1);
    LaunchpadDevice device = devices.get(0);
    assertThat(device.getType()).isEqualTo("A");

    Viewport viewport = device.getViewport();
    assertThat(viewport.getExtent().getWidth()).isEqualTo(11);
    assertThat(viewport.getExtent().getHeight()).isEqualTo(11);
  }

  @Test
  public void testGetDevicesMultipleInstances() throws Exception {
    List<LaunchpadDevice> devices = deviceProvider.getDevices("E");
    List<String> deviceTypes =
        devices.stream().map(LaunchpadDevice::getType).collect(Collectors.toList());
    assertThat(deviceTypes).containsExactly("E", "E", "E");
  }

  // Test instances of Midi devices, info, and specs.

  private class FakeSpecProvider implements MidiDeviceSpecProvider {
    private List<MidiDeviceSpec> specs = new ArrayList<>();

    public FakeSpecProvider(List<String> types) {
      types.forEach(
          (type) -> {
            addSpec(type, "Description" + type);
          });
    }

    private void addSpec(String type, String signature) {
      specs.add(new FakeMidiDeviceSpec(type, signature));
    }

    public List<MidiDeviceSpec> getSpecs() {
      return specs;
    }
  }

  private class FakeDeviceProvider implements MidiDeviceProvider {
    public List<MidiDevice.Info> infos = new ArrayList<>();
    public Map<MidiDevice.Info, MidiDevice> devices = new HashMap<>();

    public FakeDeviceProvider(List<String> types) {
      types.forEach(
          (type) -> {
            addDevice("FakeDevice" + type, "TestDescription" + type);
          });
    }

    private void addDevice(String name, String description) {
      MidiDevice.Info receiverInfo = new FakeDeviceInfo(name, description);
      MidiDevice.Info transmitterInfo = new FakeDeviceInfo(name, description);
      MidiDevice receiverDevice = new FakeReceiverDevice(receiverInfo);
      MidiDevice transmitterDevice = new FakeTransmitterDevice(transmitterInfo);

      infos.add(receiverInfo);
      infos.add(transmitterInfo);
      devices.put(receiverInfo, receiverDevice);
      devices.put(transmitterInfo, transmitterDevice);
    }

    public MidiDevice.Info[] getMidiDeviceInfo() {
      return infos.toArray(new MidiDevice.Info[0]);
    }

    public MidiDevice getMidiDevice(MidiDevice.Info info) throws MidiUnavailableException {
      return devices.get(info);
    }
  }

  private static class FakeDeviceInfo extends MidiDevice.Info {
    public FakeDeviceInfo(String name, String description) {
      // TODO(mpsalisbury) how to test for different platforms?
      super("CoreMIDI4J - " + name, "", description, "");
    }
  }

  private abstract static class FakeDevice implements MidiDevice {
    private MidiDevice.Info info;
    private boolean isOpen = false;

    public FakeDevice(MidiDevice.Info info) {
      this.info = info;
    }

    public MidiDevice.Info getDeviceInfo() {
      return info;
    }

    public long getMicrosecondPosition() {
      return 0;
    }

    public boolean isOpen() {
      return isOpen;
    }

    public void open() {
      isOpen = true;
    }

    public void close() {
      isOpen = false;
    }

    public List<Receiver> getReceivers() {
      return null;
    }

    public List<Transmitter> getTransmitters() {
      return null;
    }
  }

  private class FakeReceiverDevice extends FakeDevice {
    FakeReceiverDevice(MidiDevice.Info info) {
      super(info);
    }

    public int getMaxReceivers() {
      return -1;
    }

    public int getMaxTransmitters() {
      return 0;
    }

    public Receiver getReceiver() {
      return mockReceiver;
    }

    public Transmitter getTransmitter() {
      return null;
    }
  }

  private class FakeTransmitterDevice extends FakeDevice {
    FakeTransmitterDevice(MidiDevice.Info info) {
      super(info);
    }

    public int getMaxReceivers() {
      return 0;
    }

    public int getMaxTransmitters() {
      return -1;
    }

    public Receiver getReceiver() {
      return null;
    }

    public Transmitter getTransmitter() {
      return mockTransmitter;
    }
  }

  public class FakeMidiDeviceSpec implements MidiDeviceSpec {
    private String type;
    private String signature;

    public FakeMidiDeviceSpec(String type, String signature) {
      this.type = type;
      this.signature = signature;
    }

    public String getType() {
      return type;
    }

    public String getSignature() {
      return signature;
    }

    public LaunchpadProtocolClient newProtocolClient(Receiver receiver) {
      return mockProtocolClient;
    }

    public Receiver newProtocolReceiver(LaunchpadProtocolListener listener) {
      return mockProtocolReceiver;
    }
  }
}
