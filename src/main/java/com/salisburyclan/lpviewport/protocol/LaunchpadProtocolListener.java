package com.salisburyclan.lpviewport.protocol;

//import javax.sound.midi.MidiMessage;

public interface LaunchpadProtocolListener {
  void onButtonPressed(int pos, long timestamp);
  void onButtonReleased(int pos, long timestamp);
//  void onUnhandledMessageReceived(MidiMessage message, long timestamp);
}
