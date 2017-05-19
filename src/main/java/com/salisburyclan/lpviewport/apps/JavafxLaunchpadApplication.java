package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.LaunchpadDevice;
import com.salisburyclan.lpviewport.api.LaunchpadDeviceProvider;
import com.salisburyclan.lpviewport.api.LayoutProvider;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.device.ProdDeviceProvider;
import com.salisburyclan.lpviewport.layout.ProdLayoutProvider;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class JavafxLaunchpadApplication extends Application {

  private static final String DEVICE_SPEC_FLAG_NAME = "device";
  private static final String LAYOUT_SPEC_FLAG_NAME = "layout";
  private static final String DEFAULT_DEVICE_SPEC = "javafx";
  private static final String DEFAULT_LAYOUT_SPEC = "pickone";
  private LaunchpadDeviceProvider deviceProvider;
  private LayoutProvider layoutProvider;

  @Override
  public void init() {
    deviceProvider = ProdDeviceProvider.getEverythingProvider();
    layoutProvider = ProdLayoutProvider.getEverythingProvider();
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      run();
    } catch (RuntimeException e) {
      System.err.println("Aborting: " + e.getMessage());
      e.printStackTrace();
      Platform.exit();
    }
  }

  public abstract void run();

  // Returns first viewport using clientSpec from args.
  protected Viewport getViewport() {
    Map<String, String> parameters = getParameters().getNamed();
    String deviceSpec = parameters.getOrDefault(DEVICE_SPEC_FLAG_NAME, DEFAULT_DEVICE_SPEC);
    String layoutSpec = parameters.getOrDefault(LAYOUT_SPEC_FLAG_NAME, DEFAULT_LAYOUT_SPEC);
    return getViewport(layoutSpec, deviceSpec);
  }

  protected Viewport getViewport(String layoutSpec, String deviceSpec) {
    List<LaunchpadDevice> devices = Arrays.stream(deviceSpec.split(","))
      .flatMap(spec -> deviceProvider.getDevices(spec).stream())
      .collect(Collectors.toList());
    if (devices.isEmpty()) {
      throw new IllegalArgumentException("No Devices available for DeviceSpec: " + deviceSpec);
    }
    // TODO: consider sending deviceSpec directly to layoutProvider, or combine into a prod
    // ViewportProvider..
    return layoutProvider.createLayout(layoutSpec, devices);
  }
}
