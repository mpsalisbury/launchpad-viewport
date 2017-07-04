package com.salisburyclan.lpviewport.api;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;
import java.util.function.Function;

// Pixel is color + transparency.
// Transparency allows flexible combining of overlapping pixels colors.
@AutoValue
public abstract class Pixel {
  public static final Pixel BLACK = create(Color.BLACK, 1.0);
  public static final Pixel EMPTY = create(Color.BLACK, 0.0);
  // Tolerance allowed in equality tests.
  private static final double ALPHA_TOLERANCE = 0.00001;

  public static Pixel create(Color color) {
    return new AutoValue_Pixel(color, 1.0);
  }

  public static Pixel create(Color color, double alpha) {
    checkRange(alpha);
    if (alpha <= ALPHA_TOLERANCE) {
      // For transparent pixels, don't care about color.
      color = Color.BLACK;
    }
    return new AutoValue_Pixel(color, alpha);
  }

  public abstract Color color();

  // Transparency. 0.0 = fully transparent. 1.0 = fully opaque.
  public abstract double alpha();

  // Combines the colors and alphas of the given pixels, with the overPixel appearing
  // over this pixel.
  public Pixel combine(Pixel overPixel) {
    double underAlpha = alpha();
    double overAlpha = overPixel.alpha();

    double combinedAlpha = 1.0 - (1.0 - underAlpha) * (1.0 - overAlpha);
    Color combinedColor = Color.BLACK;
    if (combinedAlpha > 0.0) {
      combinedColor =
          Color.create(
              combineChannel(combinedAlpha, this, overPixel, Color::red),
              combineChannel(combinedAlpha, this, overPixel, Color::green),
              combineChannel(combinedAlpha, this, overPixel, Color::blue));
    }
    return create(combinedColor, combinedAlpha);
  }

  private double combineChannel(
      double combinedAlpha, Pixel underPixel, Pixel overPixel, Function<Color, Double> getChannel) {
    double underChannel = getChannel.apply(underPixel.color());
    double overChannel = getChannel.apply(overPixel.color());
    double underAlpha = underPixel.alpha();
    double overAlpha = overPixel.alpha();

    double newChannel =
        (underAlpha * (1.0 - overAlpha) * underChannel + overAlpha * overChannel) / combinedAlpha;
    return Math.max(0.0, Math.min(1.0, newChannel));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Pixel) {
      Pixel that = (Pixel) o;
      return this.color().equals(that.color())
          && DoubleMath.fuzzyEquals(this.alpha(), that.alpha(), ALPHA_TOLERANCE);
    }
    return false;
  }

  private static void checkRange(double alpha) {
    if (alpha < 0.0 || alpha > 1.0) {
      throw new IllegalArgumentException("Alpha out of range: " + alpha);
    }
  }
}
