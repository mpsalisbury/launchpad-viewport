package com.salisburyclan.launchpad.device.midi;

import com.salisburyclan.launchpad.api.ViewportListener;
import com.salisburyclan.launchpad.device.ListenerMultiplexer;
import com.salisburyclan.launchpad.protocol.LaunchpadProtocolListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to an entire device.
 */
public class DeviceListener implements LaunchpadProtocolListener {

  private ListenerMultiplexer multiplexer = new ListenerMultiplexer();

  public void addListener(ViewportListener listener) {
    multiplexer.addListener(listener);
  }

  public void onButtonPressed(int pos, long timestamp) {
    int xpos = PositionCode.getX(pos);
    int ypos = PositionCode.getY(pos);
    multiplexer.onButtonPressed(xpos, ypos);
  }

  public void onButtonReleased(int pos, long timestamp) {
    int xpos = PositionCode.getX(pos);
    int ypos = PositionCode.getY(pos);
    multiplexer.onButtonReleased(xpos, ypos);
  }
}
