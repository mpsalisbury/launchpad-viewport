package com.salisburyclan.lpviewport.util;

/** Collects cleanup commands. */
public class CleanupExecutor extends Multiplexer<Runnable> {
  public void execute() {
    getItemsCopy().forEach(command -> command.run());
  }
}
