package com.salisburyclan.lpviewport.layer;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;
import com.salisburyclan.lpviewport.api.Color;

// DColor is an RGB color with double-valued components.
@AutoValue
public abstract class DColor {
  private static final double CHANNEL_TOLERANCE = 0.00001;

  public static final DColor BLACK = create(Color.BLACK);

  public static DColor create(Color color) {
    return new AutoValue_DColor(
        (double) color.getRed() / Color.MAX_INTENSITY,
        (double) color.getGreen() / Color.MAX_INTENSITY,
        (double) color.getBlue() / Color.MAX_INTENSITY);
  }

  public static DColor create(double red, double green, double blue) {
    return new AutoValue_DColor(red, green, blue);
  }

  public abstract double red();

  public abstract double green();

  public abstract double blue();

  public Color color() {
    return Color.of(
        (int) (red() * Color.MAX_INTENSITY),
        (int) (green() * Color.MAX_INTENSITY),
        (int) (blue() * Color.MAX_INTENSITY));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof DColor) {
      DColor that = (DColor) o;
      return DoubleMath.fuzzyEquals(this.red(), that.red(), CHANNEL_TOLERANCE)
          && DoubleMath.fuzzyEquals(this.green(), that.green(), CHANNEL_TOLERANCE)
          && DoubleMath.fuzzyEquals(this.blue(), that.blue(), CHANNEL_TOLERANCE);
    }
    return false;
  }
}
