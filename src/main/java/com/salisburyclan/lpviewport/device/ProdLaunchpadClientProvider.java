package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.LaunchpadClientProvider;
import com.salisburyclan.lpviewport.device.midi.MidiLaunchpadClientProvider;
import com.salisburyclan.lpviewport.device.javafx.JavafxLaunchpadClientProvider;

public class ProdLaunchpadClientProvider extends AggregateLaunchpadClientProvider {

  public static LaunchpadClientProvider getMidiProvider() {
    ProdLaunchpadClientProvider provider = new ProdLaunchpadClientProvider();
    provider.addProvider(new MidiLaunchpadClientProvider());
    return provider;
  }

  public static LaunchpadClientProvider getJavafxProvider() {
    ProdLaunchpadClientProvider provider = new ProdLaunchpadClientProvider();
    provider.addProvider(new JavafxLaunchpadClientProvider());
    return provider;
  }

  public static LaunchpadClientProvider getEverythingProvider() {
    ProdLaunchpadClientProvider provider = new ProdLaunchpadClientProvider();
    provider.addProvider(new MidiLaunchpadClientProvider());
    provider.addProvider(new JavafxLaunchpadClientProvider());
    return provider;
  }
}
