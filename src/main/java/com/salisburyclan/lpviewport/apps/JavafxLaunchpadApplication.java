package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.api.LaunchpadClient;
import com.salisburyclan.lpviewport.api.LaunchpadClientProvider;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.device.AggregateViewport;
import com.salisburyclan.lpviewport.device.ProdLaunchpadClientProvider;

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

  // Returns first viewport using clientSpec from args.
  protected Viewport getViewport() {
    List<String> parameters = getParameters().getUnnamed();
    String clientSpec;
    if (parameters.isEmpty()) {
      clientSpec = DEFAULT_CLIENT_SPEC;
    } else {
      clientSpec = parameters.get(0);
    }
    return getViewport(clientSpec);
  }

  protected Viewport getViewport(String typeSpec) {
    List<LaunchpadClient> clients = clientProvider.getLaunchpadClients(ImmutableSet.of(typeSpec));

    if (clients.isEmpty()) {
      throw new IllegalArgumentException("Unavailable TypeSpec: " + typeSpec);
    } else if (clients.size() == 1) {
      System.out.println("Found client " + clients.get(0).getType());
      return clients.get(0).getViewport();
    } else {
      AggregateViewport.Builder builder = new AggregateViewport.Builder();
      int nextX = 0;
      for (LaunchpadClient client : clients) {
        builder.add(client.getViewport(), nextX, 0);
        nextX += client.getViewport().getExtent().getWidth();
      }
      return builder.build();
    }
  }
}
