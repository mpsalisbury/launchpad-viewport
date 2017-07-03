package com.salisburyclan.lpviewport.device.javafx;

import com.google.common.collect.ImmutableSet;
import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.LaunchpadDeviceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Produces JavaFX Devices given a deviceSpec. Supports clientSpecs of the form: javafx - one client
 * of default (8x8) size. javafx.{x}x{y} - one client of the given (x by y) size. javafx.{x}x{y}.{n}
 * - n clients of the given (x by y) size.
 *
 * <p>e.g. 'javafx.8x8.4' returns 4 8x8 javafx button grids.
 */
public class JavafxDeviceProvider implements LaunchpadDeviceProvider {

  private final String TYPE = JavafxDevice.TYPE;
  private final Pattern DEFAULT_PATTERN = Pattern.compile(TYPE);
  private final Pattern SIZE_PATTERN = Pattern.compile(TYPE + "\\.(\\d*)x(\\d*)");
  private final Pattern LINE_SIZE_PATTERN = Pattern.compile(TYPE + "\\.(\\d*)x(\\d*)\\.(\\d*)");
  //  private final Pattern GRID_SIZE_PATTERN = Pattern.compile(TYPE+"\\.(\\d*)x(\\d*)\\.(\\d*)x(\\d*)");

  @Override
  public Set<String> getAvailableTypes() {
    return ImmutableSet.of(TYPE);
  }

  @Override
  public boolean supportsDeviceSpec(String deviceSpec) {
    return DEFAULT_PATTERN.matcher(deviceSpec).matches()
        || SIZE_PATTERN.matcher(deviceSpec).matches()
        || LINE_SIZE_PATTERN.matcher(deviceSpec).matches();
  }

  @Override
  public List<LaunchpadDevice> getDevices(String deviceSpec) {
    List<LaunchpadDevice> devices = new ArrayList<>();

    Matcher defaultMatcher = DEFAULT_PATTERN.matcher(deviceSpec);
    if (defaultMatcher.matches()) {
      devices.add(new JavafxDevice());
    }
    Matcher sizeMatcher = SIZE_PATTERN.matcher(deviceSpec);
    if (sizeMatcher.matches()) {
      int xSize = Integer.parseInt(sizeMatcher.group(1));
      int ySize = Integer.parseInt(sizeMatcher.group(2));
      devices.add(new JavafxDevice(xSize, ySize));
    }
    Matcher lineSizeMatcher = LINE_SIZE_PATTERN.matcher(deviceSpec);
    if (lineSizeMatcher.matches()) {
      int xSize = Integer.parseInt(lineSizeMatcher.group(1));
      int ySize = Integer.parseInt(lineSizeMatcher.group(2));
      int count = Integer.parseInt(lineSizeMatcher.group(3));
      for (int i = 0; i < count; ++i) {
        devices.add(new JavafxDevice(xSize, ySize));
      }
    }
    return devices;
  }
}
