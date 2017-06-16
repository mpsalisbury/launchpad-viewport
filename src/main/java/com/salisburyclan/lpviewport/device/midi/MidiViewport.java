package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LightLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;

public class MidiViewport implements RawViewport {

  private LaunchpadProtocolClient client;
  private MidiListener listener;
  private LightLayer outputLayer;
  private Range2 extent;

  public MidiViewport(LaunchpadProtocolClient client, MidiListener listener) {
    this.client = client;
    this.listener = listener;
    this.outputLayer = new MidiLightLayer();
    this.extent = client.getOverallExtent();
  }

  @Override
  public LightLayer getLightLayer() {
    return outputLayer;
  }

  @Override
  public Range2 getExtent() {
    return extent;
  }

  @Override
  public void addListener(Button2Listener viewportListener) {
    listener.addListener(viewportListener);
  }

  @Override
  public void removeListener(Button2Listener viewportListener) {
    listener.removeListener(viewportListener);
  }

  private class MidiLightLayer implements LightLayer {
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
  }
}
