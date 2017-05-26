package com.salisburyclan.lpviewport.api;

public class LaunchpadException extends RuntimeException {

  public LaunchpadException(String message) {
    super(message);
  }

  public LaunchpadException(String message, Throwable cause) {
    super(message, cause);
  }

  public LaunchpadException(Throwable cause) {
    super(cause);
  }
}
