package com.salisburyclan.lpviewport.util;

/** Collects set of commands. */
public class CommandExecutor extends Multiplexer<Runnable> {
  public void execute() {
    getItemsCopy().forEach(command -> command.run());
  }
}
