package com.salisburyclan.lpviewport.apps;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.device.LaunchpadDevice;
import com.salisburyclan.lpviewport.device.LaunchpadDeviceProvider;
import com.salisburyclan.lpviewport.device.ProdDeviceProvider;
import com.salisburyclan.lpviewport.layout.LayoutProvider;
import com.salisburyclan.lpviewport.layout.ProdLayoutProvider;
import com.salisburyclan.lpviewport.viewport.RawViewport;
import com.salisburyclan.lpviewport.viewport.RawViewportViewport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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

  /** Provides the user-specified Viewport to the given callback when it has been constructed. */
  protected void getViewport(Consumer<Viewport> viewportCallback) {
    Futures.addCallback(
        getViewport(),
        new FutureCallback<Viewport>() {
          public void onSuccess(Viewport viewport) {
            viewportCallback.accept(viewport);
          }

          public void onFailure(Throwable t) {}
        });
  }

  private ListenableFuture<Viewport> getViewport() {
    return Futures.transform(getRawViewport(), rawViewport -> new RawViewportViewport(rawViewport));
  }

  // Returns first viewport using clientSpec from args.
  private ListenableFuture<RawViewport> getRawViewport() {
    Map<String, String> parameters = getParameters().getNamed();
    String deviceSpec = parameters.getOrDefault(DEVICE_SPEC_FLAG_NAME, DEFAULT_DEVICE_SPEC);
    String layoutSpec = parameters.getOrDefault(LAYOUT_SPEC_FLAG_NAME, DEFAULT_LAYOUT_SPEC);
    return getRawViewport(layoutSpec, deviceSpec);
  }

  private ListenableFuture<RawViewport> getRawViewport(String layoutSpec, String deviceSpec) {
    List<LaunchpadDevice> devices =
        Arrays.stream(deviceSpec.split(","))
            .flatMap(spec -> deviceProvider.getDevices(spec).stream())
            .collect(Collectors.toList());
    if (devices.isEmpty()) {
      throw new IllegalArgumentException("No Devices available for DeviceSpec: " + deviceSpec);
    }
    return layoutProvider.createLayout(layoutSpec, devices);
  }
}
