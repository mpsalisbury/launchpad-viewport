package com.salisburyclan.lpviewport.geom.scaler;

public abstract class Rounder {

  public enum RoundingPolicy {
    ROUND_DOWN,
    ROUND_UP,
    ROUND_CLOSEST
  }

  public static int round(float value, RoundingPolicy roundingPolicy) {
    switch (roundingPolicy) {
      case ROUND_DOWN:
        return (int) Math.floor(value);
      case ROUND_UP:
        return (int) Math.ceil(value);
      case ROUND_CLOSEST:
        return Math.round(value);
      default:
        throw new IllegalArgumentException("Invalid rounding policy");
    }
  }
}
