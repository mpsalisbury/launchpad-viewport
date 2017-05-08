package com.salisburyclan.lpviewport.testing;

/** Copy of org.junit.jupiter.api.AssertThrows.  Deprecate when we move to junit5. */
import junit.framework.AssertionFailedError;

public class AssertThrows {
  @SuppressWarnings("unchecked")
  public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
    try {
      executable.execute();
    } catch (Throwable actualException) {
      if (expectedType.isInstance(actualException)) {
        return (T) actualException;
      } else {
        String message = String.format(
            "Unexpected exception type thrown.  Expected %s, got %s.",
            expectedType.getName(), actualException.getClass().getName());
        throw new AssertionFailedError(message);
      }
    }
    throw new AssertionFailedError(
        String.format("Expected %s to be thrown, but nothing was thrown.", expectedType.getSimpleName()));
  }
}
