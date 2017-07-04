package com.salisburyclan.lpviewport.api;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.salisburyclan.lpviewport.device.ProdDeviceProvider;
import com.salisburyclan.lpviewport.layout.ProdLayoutProvider;
import com.salisburyclan.lpviewport.viewport.RawViewport;
import com.salisburyclan.lpviewport.viewport.RawViewportConstructor;
import com.salisburyclan.lpviewport.viewport.RawViewportViewport;
import java.util.Map;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

// The base class for an animated launchpad application.
// Subclass should implement run() as its entrypoint.
// Use getViewport() to request a Viewport to render your application within.
// User will specify the devices and layout that the Viewport represents via command-line arguments.
// See ProdDeviceProvider for available devices (default = javafx).
// See ProdLayoutProvider for available layouts (default = pickone).
public abstract class LaunchpadApplication extends Application {

  private static final String DEVICE_SPEC_FLAG_NAME = "device";
  private static final String LAYOUT_SPEC_FLAG_NAME = "layout";
  private static final String DEFAULT_DEVICE_SPEC = "javafx";
  private static final String DEFAULT_LAYOUT_SPEC = "pickone";
  private RawViewportConstructor viewportConstructor;

  @Override
  public void init() {
    viewportConstructor =
        new RawViewportConstructor(
            ProdDeviceProvider.getEverythingProvider(), ProdLayoutProvider.getEverythingProvider());
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

  // Entrypoint for application.
  public abstract void run();

  // Provides the user-specified Viewport to the given callback when it has been constructed.
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

  // Returns the user-specified Viewport when it has been constructed.
  protected ListenableFuture<Viewport> getViewport() {
    return Futures.transform(getRawViewport(), rawViewport -> new RawViewportViewport(rawViewport));
  }

  private ListenableFuture<RawViewport> getRawViewport() {
    Map<String, String> parameters = getParameters().getNamed();
    String deviceSpec = parameters.getOrDefault(DEVICE_SPEC_FLAG_NAME, DEFAULT_DEVICE_SPEC);
    String layoutSpec = parameters.getOrDefault(LAYOUT_SPEC_FLAG_NAME, DEFAULT_LAYOUT_SPEC);
    return viewportConstructor.getRawViewport(layoutSpec, deviceSpec);
  }
}
