package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;
import com.salisburyclan.lpviewport.viewport.ListenerMultiplexer;

/** Listens to an entire device. */
public class MidiListener implements LaunchpadProtocolListener {

  private ListenerMultiplexer multiplexer = new ListenerMultiplexer();

  public void addListener(Button2Listener listener) {
    multiplexer.addListener(listener);
  }

  public void removeListener(Button2Listener listener) {
    multiplexer.removeListener(listener);
  }

  public void onButtonPressed(int pos, long timestamp) {
    multiplexer.onButtonPressed(PositionCode.getPoint(pos));
  }

  public void onButtonReleased(int pos, long timestamp) {
    multiplexer.onButtonReleased(PositionCode.getPoint(pos));
  }
}
