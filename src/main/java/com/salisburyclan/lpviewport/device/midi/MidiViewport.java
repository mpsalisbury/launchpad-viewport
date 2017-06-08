package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;

public class MidiViewport implements Viewport {

  private LaunchpadProtocolClient client;
  private MidiListener listener;
  private Range2 extent;

  public MidiViewport(LaunchpadProtocolClient client, MidiListener listener) {
    this.client = client;
    this.listener = listener;
    this.extent = client.getOverallExtent();
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    if (extent.isPointWithin(Point.create(x, y))) {
      int pos = PositionCode.fromXY(x, y);
      int colorNum = ColorCode.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
      client.setLight(pos, colorNum);
    }
  }

  @Override
  public void setAllLights(Color color) {
    int colorNum = ColorCode.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    client.setLights(extent, colorNum);
  }

  @Override
  public void addListener(ViewportListener viewportListener) {
    listener.addListener(viewportListener);
  }

  @Override
  public void removeListener(ViewportListener viewportListener) {
    listener.removeListener(viewportListener);
  }
}
