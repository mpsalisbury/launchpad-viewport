package com.salisburyclan.launchpad.apps;

import com.salisburyclan.launchpad.api.LaunchpadClient;
import com.salisburyclan.launchpad.api.LaunchpadClientProvider;
import com.salisburyclan.launchpad.device.ProdLaunchpadClientProvider;

import com.google.common.collect.ImmutableSet;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class JavafxLaunchpadApplication extends Application {

  private static final String DEFAULT_CLIENT_SPEC = "javafx";
  private LaunchpadClientProvider clientProvider;

  @Override
  public void init() {
    clientProvider = ProdLaunchpadClientProvider.getEverythingProvider();
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

  // Returns first client using clientSpec from args.
  protected LaunchpadClient getLaunchpadClient() {
    List<String> parameters = getParameters().getUnnamed();
    String clientSpec;
    if (parameters.isEmpty()) {
      clientSpec = DEFAULT_CLIENT_SPEC;
    } else {
      clientSpec = parameters.get(0);
    }
    return getLaunchpadClient(clientSpec);
  }

  protected LaunchpadClient getLaunchpadClient(String typeSpec) {
    List<LaunchpadClient> clients = clientProvider.getLaunchpadClients(ImmutableSet.of(typeSpec));
    if (clients.isEmpty()) {
      throw new IllegalArgumentException("Unavailable TypeSpec: " + typeSpec);
    } else {
      System.out.println("Found client " + clients.get(0).getType());
      return clients.get(0);
    }
  }
}
