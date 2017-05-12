package com.salisburyclan.lpviewport.device;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewButton;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.api.ViewportListener;
import com.salisburyclan.lpviewport.api.ViewStrip;

// A Viewport for a DeviceViewport
public class ViewportAdapter implements Viewport {

  private DeviceViewport deviceViewport;

  public ViewportAdapter(DeviceViewport deviceViewport) {
    this.deviceViewport = deviceViewport;
  }

  @Override
  public ViewExtent getExtent() {
    return deviceViewport.getExtent();
  }

  @Override
  public void setLight(int x, int y, Color color) {
    deviceViewport.setLight(x, y, color);
  }

  @Override
  public void addListener(ViewportListener viewportListener) {
    deviceViewport.addListener(viewportListener);
  }

  @Override
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
