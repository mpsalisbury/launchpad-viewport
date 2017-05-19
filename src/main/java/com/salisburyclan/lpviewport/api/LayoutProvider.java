package com.salisburyclan.lpviewport.api;

import java.util.Collection;
import java.util.List;

public interface LayoutProvider {

  // Returns a list of human-readable descriptions of the
  // LayoutSpecs supported by this provider.
  List<String> getLayoutSpecDescriptions();

  // Returns true if the given layoutSpec is valid for
  // this provider.
  boolean supportsSpec(String layoutSpec);

  /**
   * Returns a Viewport composed of the provided Devices.
   * This may block waiting for user configuration feedback
   * before returning (for example, letting the user specify
   * the desired layout amongst the set of sub-viewports.)
   * See the descriptions from getLayoutSpecDescriptions()
   * for the supported forms of layoutSpecs.
   *
   * @throws IllegalArgumentException if spec is not valid.
   */
  Viewport createLayout(String layoutSpec, Collection<LaunchpadDevice> devices);
}
