package com.salisburyclan.lpviewport.api;

import java.util.List;
import java.util.Set;

public interface LaunchpadClientProvider {

  boolean supportsClientSpec(String clientSpec);

  Set<String> getAvailableTypes();

  /**
   * A clientspec is a string of the form:
   *   'type[.config]'
   * where config is an optional type-specific configuration string,
   * generally of the form 'count', meaning to choose 'count' number of
   * clients of the given type to return.  e.g. 'javafx.4x2.8x8' returns
   * a 4x2 grid of 8x8 javafx button grids.
   */
  List<LaunchpadClient> getLaunchpadClients(Set<String> clientSpecs);
}
