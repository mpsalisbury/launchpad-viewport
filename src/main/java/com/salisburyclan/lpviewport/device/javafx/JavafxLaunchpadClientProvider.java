package com.salisburyclan.lpviewport.device.javafx;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.LaunchpadClientProvider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavafxLaunchpadClientProvider implements LaunchpadClientProvider {

  private final String TYPE = JavafxLaunchpadClient.TYPE;
  private final Pattern DEFAULT_PATTERN = Pattern.compile(TYPE);
  private final Pattern SIZE_PATTERN = Pattern.compile(TYPE+"\\.(\\d*)x(\\d*)");
//  private final Pattern GRID_SIZE_PATTERN = Pattern.compile(TYPE+"\\.(\\d*)x(\\d*)\\.(\\d*)x(\\d*)");


  @Override
  public Set<String> getAvailableTypes() {
    return ImmutableSet.of(TYPE);
  }

  @Override
  public boolean supportsClientSpec(String clientSpec) {
    return DEFAULT_PATTERN.matcher(clientSpec).matches()
        || SIZE_PATTERN.matcher(clientSpec).matches();
  }

  @Override
  public List<LaunchpadClient> getLaunchpadClients(Set<String> clientSpecs) {
    List<LaunchpadClient> clients = new ArrayList<>();

    clientSpecs.forEach(clientSpec -> {
      Matcher defaultMatcher = DEFAULT_PATTERN.matcher(clientSpec);
      if (defaultMatcher.matches()) {
        clients.add(new JavafxLaunchpadClient());
      }
      Matcher sizeMatcher = SIZE_PATTERN.matcher(clientSpec);
      if (sizeMatcher.matches()) {
        int xSize = Integer.parseInt(sizeMatcher.group(1));
        int ySize = Integer.parseInt(sizeMatcher.group(2));
        clients.add(new JavafxLaunchpadClient(xSize, ySize));
      }
    });
    return clients;
  }
}
