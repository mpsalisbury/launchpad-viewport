package com.salisburyclan.lpviewport.device;

import java.util.List;
import java.util.Set;

// Provides Devices given a deviceSpec.
public interface LaunchpadDeviceProvider {

  boolean supportsDeviceSpec(String deviceSpec);

  // TODO: change to deviceSpecDescriptions?
  Set<String> getAvailableTypes();

  /**
   * @param deviceSpec a string of the form: 'type[.config]' where config is an optional
   *     type-specific configuration string.
   * @return all devices that match the given deviceSpec.
   */
  List<LaunchpadDevice> getDevices(String deviceSpec);
}
