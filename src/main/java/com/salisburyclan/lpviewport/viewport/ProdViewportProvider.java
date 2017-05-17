package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.ViewportProvider;

public class ProdViewportProvider {

  public static ViewportProvider getEverythingProvider() {
    AggregateViewportProvider provider = new AggregateViewportProvider();
    provider.addProvider(new HorizontalViewportProvider(provider));
    return provider;
  }
}
