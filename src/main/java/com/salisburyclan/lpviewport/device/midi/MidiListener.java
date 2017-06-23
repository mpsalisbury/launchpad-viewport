package com.salisburyclan.lpviewport.device.midi;

import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;
import com.salisburyclan.lpviewport.viewport.Button2ListenerMultiplexer;

/** Listens to an entire device. */
public class MidiListener implements LaunchpadProtocolListener {

  private Button2ListenerMultiplexer multiplexer = new Button2ListenerMultiplexer();

  public void addListener(Button2Listener listener) {
    multiplexer.add(listener);
  }

  public void removeListener(Button2Listener listener) {
    multiplexer.remove(listener);
  }

  public void onButtonPressed(int pos, long timestamp) {
    multiplexer.onButtonPressed(PositionCode.getPoint(pos));
  }

  public void onButtonReleased(int pos, long timestamp) {
    multiplexer.onButtonReleased(PositionCode.getPoint(pos));
  }
}
