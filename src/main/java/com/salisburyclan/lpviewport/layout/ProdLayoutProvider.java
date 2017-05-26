package com.salisburyclan.lpviewport.layout;

import com.salisburyclan.lpviewport.api.LayoutProvider;

public class ProdLayoutProvider {

  public static LayoutProvider getEverythingProvider() {
    AggregateLayoutProvider provider = new AggregateLayoutProvider();
    provider.addProvider(new PickOneLayoutProvider());
    provider.addProvider(new HorizontalLayoutProvider());
    provider.addProvider(new LinkedPairsLayoutProvider());
    return provider;
  }
}
