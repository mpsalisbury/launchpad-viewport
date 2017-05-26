package com.salisburyclan.lpviewport.api;

//import javax.sound.midi.MidiMessage;

public interface ViewportListener {
  void onButtonPressed(int x, int y);

  void onButtonReleased(int x, int y);
  //  void onUnhandledMessageReceived(MidiMessage message);
}
