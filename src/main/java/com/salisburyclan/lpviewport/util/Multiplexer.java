package com.salisburyclan.lpviewport.util;

import java.util.ArrayList;
import java.util.List;

public class Multiplexer<ItemT> {

  private List<ItemT> items = new ArrayList<>();

  public void add(ItemT item) {
    items.add(item);
  }

  public void remove(ItemT item) {
    items.remove(item);
  }

  public int getNumItems() {
    return items.size();
  }

  public void clear() {
    items.clear();
  }

  // Return defensive copy of listeners for subclasses to iterate over.
  protected List<ItemT> getItemsCopy() {
    return new ArrayList<>(items);
  }
}
