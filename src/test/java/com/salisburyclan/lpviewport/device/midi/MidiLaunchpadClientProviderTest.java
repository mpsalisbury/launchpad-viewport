package com.salisburyclan.launchpad.device.midi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.Viewport;
import com.salisburyclan.launchpad.midi.MidiDeviceProvider;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;
import com.salisburyclan.launchpad.protocol.ViewExtent;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

// TODO: Cleanup entire class
//
@RunWith(JUnit4.class)
public class MidiLaunchpadClientProviderTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Receiver mockReceiver;
  @Mock private Transmitter mockTransmitter;
  @Mock private LaunchpadProtocolClient mockProtocolClient;
  @Mock private Receiver mockProtocolReceiver;

  private MidiLaunchpadClientProvider clientProvider;

  @Before
  public void setUp() {
    clientProvider = new MidiLaunchpadClientProvider(
        new FakeDeviceProvider(ImmutableList.of("A", "C", "D", "E", "E", "E")),
        new FakeSpecProvider(ImmutableList.of("A", "B", "C", "E")));

    when(mockProtocolClient.getOverallExtent()).thenReturn(new ViewExtent(0,10,0,10));
  }

  @Test
  public void testGetLaunchpadClientsOneType() throws Exception {
    List<LaunchpadClient> clients = clientProvider.getLaunchpadClients(ImmutableSet.of("A"));
    assertThat(clients.size()).isEqualTo(1);
    LaunchpadClient client = clients.get(0);
    assertThat(client.getType()).isEqualTo("A");

    Viewport viewport = client.getViewport();
    assertThat(viewport.getWidth()).isEqualTo(11);
    assertThat(viewport.getHeight()).isEqualTo(11);
  }

  @Test
  public void testGetLaunchpadClientsMultipleTypes() throws Exception {
    List<LaunchpadClient> clients = clientProvider.getLaunchpadClients(ImmutableSet.of("A", "C", "D"));
    List<String> clientTypes = clients.stream().map(LaunchpadClient::getType).collect(Collectors.toList());
    assertThat(clientTypes).containsExactly("A", "C");
  }

  @Test
  public void testGetLaunchpadClientsOneTypeMultipleInstances() throws Exception {
    List<LaunchpadClient> clients = clientProvider.getLaunchpadClients(ImmutableSet.of("E"));
    List<String> clientTypes = clients.stream().map(LaunchpadClient::getType).collect(Collectors.toList());
    assertThat(clientTypes).containsExactly("E", "E", "E");
  }


  // Test instances of Midi devices, info, and specs.
 
  private class FakeSpecProvider implements MidiDeviceSpecProvider {
    private List<MidiDeviceSpec> specs = new ArrayList<>();

    public FakeSpecProvider(List<String> types) {
      types.forEach((type) -> {
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
      types.forEach((type) -> {
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

    public MidiDevice getMidiDevice(MidiDevice.Info info)
        throws MidiUnavailableException {
      return devices.get(info);
    }
  }

  private static class FakeDeviceInfo extends MidiDevice.Info {
    public FakeDeviceInfo(String name, String description) {
      super(name, "", description, "");
    }
  }

  private static abstract class FakeDevice implements MidiDevice {
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
