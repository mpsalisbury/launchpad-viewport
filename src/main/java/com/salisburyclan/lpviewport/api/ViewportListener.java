package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.geom.Point;
//import javax.sound.midi.MidiMessage;

public interface ViewportListener {
  void onButtonPressed(Point p);

  void onButtonReleased(Point p);
  //  void onUnhandledMessageReceived(MidiMessage message);
}
