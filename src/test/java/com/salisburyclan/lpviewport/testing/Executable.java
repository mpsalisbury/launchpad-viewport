package com.salisburyclan.launchpad.testing;

/** Copy of org.junit.jupiter.api.Executable.  Deprecate when we move to junit5. */

@FunctionalInterface
public interface Executable {
  void execute() throws Throwable;
}
