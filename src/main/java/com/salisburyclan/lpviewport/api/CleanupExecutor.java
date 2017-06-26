package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.util.Multiplexer;

/** Collects cleanup commands. */
public class CleanupExecutor extends Multiplexer<Runnable> {
  public void execute() {
    getItemsCopy().forEach(command -> command.run());
  }
}
