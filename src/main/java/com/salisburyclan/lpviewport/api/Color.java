package com.salisburyclan.lpviewport.api;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;

// An RGB color with (0.0..1.0)-valued components.
@AutoValue
public abstract class Color {
  // Per-channel tolerance used for equality tests.
  private static final double CHANNEL_TOLERANCE = 0.00001;

  // Common colors
  public static final Color BLACK = create(0, 0, 0);
  public static final Color BLUE = create(0, 0, 1);
  public static final Color BROWN = create(0.55, 0.27, 0.07);
  public static final Color CYAN = create(0, 1, 1);
  public static final Color DARK_GRAY = create(0.08, 0.08, 0.08);
  public static final Color GRAY = create(0.22, 0.22, 0.22);
  public static final Color GREEN = create(0, 1, 0);
  public static final Color LIGHT_GRAY = create(0.45, 0.45, 0.45);
  public static final Color MAGENTA = create(1, 0, 1);
  public static final Color ORANGE = create(1, 0.22, 0);
  public static final Color PINK = create(1, 0.22, 0.50);
  public static final Color PURPLE = create(0.50, 0, 0.88);
  public static final Color RED = create(1, 0, 0);
  public static final Color SKY_BLUE = create(0.45, 0.85, 0.66);
  public static final Color WHITE = create(1, 1, 1);
  public static final Color YELLOW = create(1, 1, 0);
  public static final Color YELLOW_GREEN = create(0.35, 1, 0);

  // Constructs a new Color instance.
  public static Color create(double red, double green, double blue) {
    checkRange(red);
    checkRange(green);
    checkRange(blue);
    return new AutoValue_Color(red, green, blue);
  }

  public abstract double red();

  public abstract double green();

  public abstract double blue();

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Color) {
      Color that = (Color) o;
      return DoubleMath.fuzzyEquals(this.red(), that.red(), CHANNEL_TOLERANCE)
          && DoubleMath.fuzzyEquals(this.green(), that.green(), CHANNEL_TOLERANCE)
          && DoubleMath.fuzzyEquals(this.blue(), that.blue(), CHANNEL_TOLERANCE);
    }
    return false;
  }

  private static void checkRange(double component) {
    if (component < 0.0 || component > 1.0) {
      throw new IllegalArgumentException("Color Component out of range: " + component);
    }
  }
}
