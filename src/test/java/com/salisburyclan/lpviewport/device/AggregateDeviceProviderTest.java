package com.salisburyclan.lpviewport.device;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.truth.Truth8;
import com.salisburyclan.lpviewport.viewport.RawViewport;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AggregateDeviceProviderTest {

  private AggregateDeviceProvider deviceProvider;

  @Before
  public void setUp() {
    deviceProvider = new AggregateDeviceProvider();
    deviceProvider.addProvider(new FakeDeviceProvider(ImmutableSet.of("A", "B")));
    deviceProvider.addProvider(new FakeDeviceProvider(ImmutableSet.of("B", "C")));
    deviceProvider.addProvider(new FakeDeviceProvider(ImmutableSet.of("D", "E", "B")));
  }

  @Test
  public void testGetAvailableTypes() throws Exception {
    assertThat(deviceProvider.getAvailableTypes())
        .containsExactly("A", "B", "C", "D", "E")
        .inOrder();
  }

  @Test
  public void testGetLaunchpadClientsOneType() throws Exception {
    Truth8.assertThat(deviceProvider.getDevices("A").stream().map(LaunchpadDevice::getType))
        .containsExactly("A");
  }

  @Test
  public void testGetLaunchpadClientsMultipleDevices() throws Exception {
    Truth8.assertThat(deviceProvider.getDevices("B").stream().map(LaunchpadDevice::getType))
        .containsExactly("B", "B", "B");
  }

  private static class FakeDeviceProvider implements LaunchpadDeviceProvider {
    private Set<String> types;

    public FakeDeviceProvider(Set<String> types) {
      this.types = types;
    }

    @Override
    public boolean supportsDeviceSpec(String deviceSpec) {
      // TODO test
      return types.contains(deviceSpec);
    }

    @Override
    public Set<String> getAvailableTypes() {
      return types;
    }

    @Override
    public List<LaunchpadDevice> getDevices(String typeSpec) {
      if (types.contains(typeSpec)) {
        return ImmutableList.of(new FakeLaunchpadDevice(typeSpec));
      } else {
        return ImmutableList.of();
      }
    }
  }

  private static class FakeLaunchpadDevice implements LaunchpadDevice {
    private String type;

    public FakeLaunchpadDevice(String type) {
      this.type = type;
    }

    @Override
    public String getType() {
      return type;
    }

    @Override
    public RawViewport getViewport() {
      return null;
    }

    @Override
    public void close() {}
  }
}
