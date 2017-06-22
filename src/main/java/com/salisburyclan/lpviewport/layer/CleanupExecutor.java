package com.salisburyclan.lpviewport.layer;

import java.util.ArrayList;
import java.util.List;

/** Collects cleanup commands. */
public class CleanupExecutor {

  private List<Runnable> commands = new ArrayList<>();

  public void add(Runnable command) {
    commands.add(command);
  }

  public void remove(Runnable command) {
    commands.remove(command);
  }

  public void execute() {
    List<Runnable> commandsDefensiveCopy = new ArrayList<>(commands);
    commandsDefensiveCopy.forEach(command -> command.run());
  }
}
