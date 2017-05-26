package com.salisburyclan.lpviewport.device.midi.mk2;

public class LaunchpadMk2Constants {

  // ButtonMapping encodes correspondences between the Position(x,y) of a button and
  // the encoded midi note location for this device.
  public static class ButtonMapping {
    public int pos; // encoded x,y position
    public int note; // midi note for position

    public ButtonMapping(int pos, int note) {
      this.pos = pos;
      this.note = note;
    }
  }

  // Mk2 button mappings
  public static ButtonMapping[] buttonMappings = {
    new ButtonMapping(0x0000, 11),
    new ButtonMapping(0x0700, 18),
    new ButtonMapping(0x0007, 81),
    new ButtonMapping(0x0707, 88),
    new ButtonMapping(0x0406, 75),
    new ButtonMapping(0x0800, 19),
    new ButtonMapping(0x0807, 89),
    new ButtonMapping(0x0008, 104),
    new ButtonMapping(0x0708, 111)
  };

  // ColorMapping encodes correspondences between an encoded Color(r,g,b) and
  // the encoded individual r, g, b values for this device.
  public static class ColorMapping {
    public int color; // encoded r,g,b
    public int red; // device red
    public int green; // device green
    public int blue; // device blue

    public ColorMapping(int color, int red, int green, int blue) {
      this.color = color;
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
  }

  // Mk2 color mappings
  public static ColorMapping[] colorMappings = {
    new ColorMapping(0x000000, 0, 0, 0),
    new ColorMapping(0x010000, 1, 0, 0),
    new ColorMapping(0x000100, 0, 1, 0),
    new ColorMapping(0x000001, 0, 0, 1),
    new ColorMapping(0x3f0000, 63, 0, 0),
    new ColorMapping(0x003f00, 0, 63, 0),
    new ColorMapping(0x00003f, 0, 0, 63),
    new ColorMapping(0x3f3f3f, 63, 63, 63),
  };
}
