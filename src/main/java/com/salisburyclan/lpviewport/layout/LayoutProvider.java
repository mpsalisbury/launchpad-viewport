package com.salisburyclan.lpviewport.layout;

import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.viewport.RawViewport;
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
   * Returns a Future Viewport composed of the provided Devices. Future may wait for user
   * configuration feedback before populating the value (for example, letting the user specify the
   * desired layout amongst the set of sub-viewports.) See the descriptions from
   * getLayoutSpecDescriptions() for the supported forms of layoutSpecs.
   *
   * @throws IllegalArgumentException if spec is not valid.
   */
  ListenableFuture<RawViewport> createLayout(
      String layoutSpec, Collection<LaunchpadDevice> devices);
}
