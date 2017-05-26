package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.LaunchpadDeviceProvider;
import com.salisburyclan.lpviewport.device.javafx.JavafxDeviceProvider;
import com.salisburyclan.lpviewport.device.midi.MidiLaunchpadDeviceProvider;

public class ProdDeviceProvider extends AggregateDeviceProvider {

  private ProdDeviceProvider() {}

  public static LaunchpadDeviceProvider getMidiProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new MidiLaunchpadDeviceProvider());
    return provider;
  }

  public static LaunchpadDeviceProvider getJavafxProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new JavafxDeviceProvider());
    return provider;
  }

  public static LaunchpadDeviceProvider getEverythingProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new MidiLaunchpadDeviceProvider());
    provider.addProvider(new JavafxDeviceProvider());
    return provider;
  }
}
