package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.geom.Range2;

// A buffer of colored pixels for staging images.
public class LayerBuffer {
  private Range2 extent;
  private Pixel[][] buffer;

  public LayerBuffer(Range2 extent) {
    this.extent = extent;
    this.buffer = new Pixel[extent.getWidth()][extent.getHeight()];
    setAllPixels(Pixel.EMPTY);
  }

  public Range2 getExtent() {
    return extent;
  }

  public void setPixel(int x, int y, DColor color) {
    setPixel(x, y, Pixel.create(color));
  }

  public void setPixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      buffer[x][y] = pixel;
    }
  }

  // Place given pixel on top of existing pixel at location (x,y).
  public void combinePixel(int x, int y, Pixel pixel) {
    if (extent.isPointWithin(x, y)) {
      buffer[x][y] = buffer[x][y].combine(pixel);
    }
  }

  public Pixel getPixel(int x, int y) {
    if (extent.isPointWithin(x, y)) {
      return buffer[x][y];
    } else {
      return Pixel.EMPTY; // TODO error instead?
    }
  }

  private void setAllPixels(Pixel pixel) {
    extent
        .xRange()
        .stream()
        .forEach(
            x -> {
              extent.yRange().stream().forEach(y -> setPixel(x, y, pixel));
            });
  }
}
