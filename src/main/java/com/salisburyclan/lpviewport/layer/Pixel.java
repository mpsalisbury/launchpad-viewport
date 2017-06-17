package com.salisburyclan.lpviewport.layer;

import com.google.auto.value.AutoValue;
import com.google.common.math.DoubleMath;
import com.salisburyclan.lpviewport.api.Color;
import java.util.function.Function;

// Pixel is color + transparency.
// Transparency allows flexible combining of overlapping pixels colors.
@AutoValue
public abstract class Pixel {
  public static final Pixel BLACK = create(DColor.BLACK, 1.0);
  public static final Pixel EMPTY = create(DColor.BLACK, 0.0);
  private static final double ALPHA_TOLERANCE = 0.00001;

  public static Pixel create(Color color) {
    return new AutoValue_Pixel(DColor.create(color), 1.0);
  }

  public static Pixel create(Color color, double alpha) {
    return new AutoValue_Pixel(DColor.create(color), alpha);
  }

  public static Pixel create(DColor color) {
    return new AutoValue_Pixel(color, 1.0);
  }

  public static Pixel create(DColor color, double alpha) {
    return new AutoValue_Pixel(color, alpha);
  }

  public abstract DColor color();

  // Transparency. 0.0 = fully transparent. 1.0 = fully opaque.
  public abstract double alpha();

  // Combines the colors and alphas of the given pixels, with the overPixel appearing
  // over this pixel.
  public Pixel combine(Pixel overPixel) {
    double underAlpha = alpha();
    double overAlpha = overPixel.alpha();

    double combinedAlpha = 1.0 - (1.0 - underAlpha) * (1.0 - overAlpha);
    DColor combinedColor = DColor.BLACK;
    if (combinedAlpha > 0.0) {
      combinedColor =
          DColor.create(
              combineChannel(combinedAlpha, this, overPixel, DColor::red),
              combineChannel(combinedAlpha, this, overPixel, DColor::green),
              combineChannel(combinedAlpha, this, overPixel, DColor::blue));
    }
    return create(combinedColor, combinedAlpha);
  }

  private double combineChannel(
      double combinedAlpha,
      Pixel underPixel,
      Pixel overPixel,
      Function<DColor, Double> getChannel) {
    double underChannel = getChannel.apply(underPixel.color());
    double overChannel = getChannel.apply(overPixel.color());
    double underAlpha = underPixel.alpha();
    double overAlpha = overPixel.alpha();

    return (underAlpha * (1.0 - overAlpha) * underChannel + overAlpha * overChannel)
        / combinedAlpha;
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
}