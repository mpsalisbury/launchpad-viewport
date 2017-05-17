package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.DeviceProvider;
import com.salisburyclan.lpviewport.device.midi.MidiDeviceProvider;
import com.salisburyclan.lpviewport.device.javafx.JavafxDeviceProvider;

public class ProdDeviceProvider extends AggregateDeviceProvider {

  private ProdDeviceProvider() {}

  public static DeviceProvider getMidiProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new MidiDeviceProvider());
    return provider;
  }

  public static DeviceProvider getJavafxProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new JavafxDeviceProvider());
    return provider;
  }

  public static DeviceProvider getEverythingProvider() {
    AggregateDeviceProvider provider = new AggregateDeviceProvider();
    provider.addProvider(new MidiDeviceProvider());
    provider.addProvider(new JavafxDeviceProvider());
    return provider;
  }
}
