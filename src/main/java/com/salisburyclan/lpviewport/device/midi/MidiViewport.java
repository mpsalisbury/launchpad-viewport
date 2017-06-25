package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.RawLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolClient;

public class MidiViewport implements RawViewport {

  private LaunchpadProtocolClient client;
  private MidiListener listener;
  private RawLayer outputLayer;
  private Range2 extent;

  public MidiViewport(LaunchpadProtocolClient client, MidiListener listener) {
    this.client = client;
    this.listener = listener;
    this.outputLayer = new MidiRawLayer();
    this.extent = client.getOverallExtent();
  }

  @Override
  public RawLayer getRawLayer() {
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

  private class MidiRawLayer implements RawLayer {
    @Override
    public Range2 getExtent() {
      return extent;
    }

    @Override
    public void setPixel(int x, int y, DColor color) {
      if (extent.isPointWithin(Point.create(x, y))) {
        int pos = PositionCode.fromXY(x, y);
        int colorNum = ColorCode.fromRGB(color.red(), color.green(), color.blue());
        client.setLight(pos, colorNum);
      }
    }

    @Override
    public void setAllPixels(DColor color) {
      int colorNum = ColorCode.fromRGB(color.red(), color.green(), color.blue());
      client.setLights(extent, colorNum);
    }
  }
}
