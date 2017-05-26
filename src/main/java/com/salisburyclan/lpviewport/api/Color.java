package com.salisburyclan.lpviewport.api;

// RGB color, each in range 0..63.
public final class Color {

  // Minimal component intensity
  public static final int MIN_INTENSITY = 0;
  // Maximal component intensity
  public static final int MAX_INTENSITY = 63;

  // Common colors
  public static final Color BLACK = Color.of(0, 0, 0);
  public static final Color BLUE = Color.of(0, 0, 63);
  public static final Color CYAN = Color.of(0, 63, 63);
  public static final Color DARK_GRAY = Color.of(4, 4, 4);
  public static final Color GRAY = Color.of(14, 14, 14);
  public static final Color GREEN = Color.of(0, 63, 0);
  public static final Color LIGHT_GRAY = Color.of(30, 30, 30);
  public static final Color MAGENTA = Color.of(63, 0, 63);
  public static final Color ORANGE = Color.of(63, 13, 0);
  public static final Color PINK = Color.of(63, 14, 31);
  public static final Color PURPLE = Color.of(32, 0, 56);
  public static final Color RED = Color.of(63, 0, 0);
  public static final Color SKY_BLUE = Color.of(28, 55, 41);
  public static final Color WHITE = Color.of(63, 63, 63);
  public static final Color YELLOW = Color.of(63, 63, 0);
  public static final Color YELLOW_GREEN = Color.of(23, 63, 0);

  public static Color of(int red, int green, int blue) {
    if (red < MIN_INTENSITY || red > MAX_INTENSITY) {
      throw new IllegalArgumentException(
          "Invalid red value : " + red + ". Acceptable values are in range [0..63].");
    }
    if (green < MIN_INTENSITY || green > MAX_INTENSITY) {
      throw new IllegalArgumentException(
          "Invalid green value : " + green + ". Acceptable values are in range [0..63].");
    }
    if (blue < MIN_INTENSITY || blue > MAX_INTENSITY) {
      throw new IllegalArgumentException(
          "Invalid blue value : " + blue + ". Acceptable values are in range [0..63].");
    }
    return new Color(red, green, blue);
  }

  // The red component intensity
  private final int red;
  // The green component intensity
  private final int green;
  // The blue component intensity
  private final int blue;

  /**
   * @param red The red component
   * @param green The green component
   * @param blue The blue component
   */
  private Color(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  private Color(java.awt.Color javaColor) {
    this.red = javaColor.getRed() >> 2;
    this.green = javaColor.getGreen() >> 2;
    this.blue = javaColor.getBlue() >> 2;
  }

  /** @return the red intensity */
  public int getRed() {
    return red;
  }

  /** @return the green intensity */
  public int getGreen() {
    return green;
  }

  /** @return the blue intensity */
  public int getBlue() {
    return blue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Color color = (Color) o;
    return blue == color.blue && green == color.green && red == color.red;
  }

  @Override
  public int hashCode() {
    int result = red;
    result = 31 * result + green;
    result = 31 * result + blue;
    return result;
  }
}
