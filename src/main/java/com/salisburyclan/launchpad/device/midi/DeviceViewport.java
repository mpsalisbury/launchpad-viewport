package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.Color;
import com.salisburyclan.launchpad.api.Viewport;
import com.salisburyclan.launchpad.api.ViewButton;
import com.salisburyclan.launchpad.api.ViewExtent;
import com.salisburyclan.launchpad.api.ViewportListener;
import com.salisburyclan.launchpad.api.ViewStrip;
import com.salisburyclan.launchpad.device.SubViewport;
import com.salisburyclan.launchpad.device.SubViewStrip;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolClient;

public class DeviceViewport implements Viewport {

  private LaunchpadProtocolClient client;
  private DeviceListener listener;
  private ViewExtent extent;

  public DeviceViewport(LaunchpadProtocolClient client, DeviceListener listener) {
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
  //public void setLights(int xLow, int xHigh, int yLow, int yHigh, Color color);
  //public void setAllLights(Color color);

  // Adds a listener for the button at the given position.
  //public void addListener(int x, int y, ButtonListener listener);

  // Adds a listener for this viewport.
  @Override
  public void addListener(ViewportListener viewportListener) {
    listener.addListener(viewportListener);
  }

  // Returns a new viewport relative to this one.
  public Viewport getSubViewport(ViewExtent extent) {
    return new SubViewport(this, extent);
  }

  @Override
  public ViewStrip getSubViewStrip(ViewExtent extent) {
    return new SubViewStrip(this, extent);
  }

  @Override
  public ViewButton getSubViewButton(int x, int y) {
    return new SubViewport.SubViewButton(this, x, y);
  }
}
