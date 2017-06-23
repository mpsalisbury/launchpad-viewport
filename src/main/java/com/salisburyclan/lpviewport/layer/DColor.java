package com.salisburyclan.lpviewport.layer;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;
import com.salisburyclan.lpviewport.api.Color;

// DColor is an RGB color with double-valued components.
@AutoValue
public abstract class DColor {
  private static final double CHANNEL_TOLERANCE = 0.00001;

  // Common colors
  public static final DColor BLACK = create(0, 0, 0);
  public static final DColor BLUE = create(0, 0, 1);
  public static final DColor CYAN = create(0, 1, 1);
  public static final DColor DARK_GRAY = create(0.08, 0.08, 0.08);
  public static final DColor GRAY = create(0.22, 0.22, 0.22);
  public static final DColor GREEN = create(0, 1, 0);
  public static final DColor LIGHT_GRAY = create(0.45, 0.45, 0.45);
  public static final DColor MAGENTA = create(1, 0, 1);
  public static final DColor ORANGE = create(1, 0.22, 0);
  public static final DColor PINK = create(1, 0.22, 0.50);
  public static final DColor PURPLE = create(0.50, 0, 0.88);
  public static final DColor RED = create(1, 0, 0);
  public static final DColor SKY_BLUE = create(0.45, 0.85, 0.66);
  public static final DColor WHITE = create(1, 1, 1);
  public static final DColor YELLOW = create(1, 1, 0);
  public static final DColor YELLOW_GREEN = create(0.35, 1, 0);

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
