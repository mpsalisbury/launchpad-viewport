package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;
import com.salisburyclan.lpviewport.device.DeviceViewport;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;

public class MidiViewport implements DeviceViewport {

  private LaunchpadProtocolClient client;
  private MidiListener listener;
  private ViewExtent extent;

  public MidiViewport(LaunchpadProtocolClient client, MidiListener listener) {
    this.client = client;
    this.listener = listener;
    this.extent = client.getOverallExtent();
  }

  @Override
  public ViewExtent getExtent() {
    return extent;
  }

  @Override
  public void setLight(int x, int y, Color color) {
    extent.assertPointWithin(x, y);
    int pos = PositionCode.fromXY(x, y);
    int colorNum = ColorCode.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    client.setLight(pos, colorNum);
  }

  @Override
  public void addListener(ViewportListener viewportListener) {
    listener.addListener(viewportListener);
  }
}