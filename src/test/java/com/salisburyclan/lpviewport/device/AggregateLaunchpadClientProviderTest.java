package com.salisburyclan.lpviewport.device;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.truth.Truth8;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.LaunchpadClientProvider;
import com.salisburyclan.lpviewport.api.Viewport;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class AggregateLaunchpadClientProviderTest {

  private AggregateLaunchpadClientProvider clientProvider;

  @Before
  public void setUp() {
    clientProvider = new AggregateLaunchpadClientProvider();
    clientProvider.addProvider(
        new FakeLaunchpadClientProvider(ImmutableSet.of("A", "B")));
    clientProvider.addProvider(
        new FakeLaunchpadClientProvider(ImmutableSet.of("B", "C")));
    clientProvider.addProvider(
        new FakeLaunchpadClientProvider(ImmutableSet.of("D", "E", "B")));
  }

  @Test
  public void testGetAvailableTypes() throws Exception {
    assertThat(clientProvider.getAvailableTypes())
      .containsExactly("A", "B", "C", "D", "E");
  }

  @Test
  public void testGetLaunchpadClientsOneType() throws Exception {
    Truth8.assertThat(clientProvider.getLaunchpadClients(ImmutableSet.of("A")).stream()
      .map(LaunchpadClient::getType))
      .containsExactly("A");
  }

  @Test
  public void testGetLaunchpadClientsMultipleTypes() throws Exception {
    Truth8.assertThat(clientProvider.getLaunchpadClients(ImmutableSet.of("A", "B")).stream()
      .map(LaunchpadClient::getType))
      .containsExactly("A", "B", "B", "B");
  }

  private static class FakeLaunchpadClientProvider implements LaunchpadClientProvider {
    private Set<String> types;

    public FakeLaunchpadClientProvider(Set<String> types) {
      this.types = types;
    }

    @Override
    public Set<String> getAvailableTypes() {
      return types;
    }

    @Override
    public List<LaunchpadClient> getLaunchpadClients(Set<String> typeSpecs) {
      return Sets.intersection(types, typeSpecs).stream()
        .map(FakeLaunchpadClient::new)
        .collect(Collectors.toList());
    }
  }

  private static class FakeLaunchpadClient implements LaunchpadClient {
    private String type;

    public FakeLaunchpadClient(String type) {
      this.type = type;
    }

    @Override
    public String getType() {
      return type;
    }

    @Override
    public Viewport getViewport() {
      return null;
    }

    @Override
    public void close() {
    }
  }
}
