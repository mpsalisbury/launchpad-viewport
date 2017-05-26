package com.salisburyclan.lpviewport.device.midi.mk2;

import com.salisburyclan.lpviewport.device.midi.LaunchpadDevice;
import com.salisburyclan.lpviewport.protocol.LaunchpadProtocolListener;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class LaunchpadMk2ProtocolReceiver implements Receiver {

  /** The listener to notify when commands are received. */
  private final LaunchpadProtocolListener listener;

  private final LaunchpadDevice device = new LaunchpadMk2Device();

  /** @param listener The listener to to notify when commands are received. Must not be null. */
  public LaunchpadMk2ProtocolReceiver(LaunchpadProtocolListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener must not be null.");
    }
    this.listener = listener;
  }

  @Override
  public void send(MidiMessage message, long timestamp) {
    if (message instanceof ShortMessage) {
      handleShortMessage((ShortMessage) message, timestamp);
      //    } else {
      //      listener.onUnhandledMessageReceived(message, timestamp);
    }
  }

  /**
   * Parses and routes MIDI short messages to adequate sub-handlers.
   *
   * @param message The message to handle.
   * @param timestamp When the message arrived.
   */
  protected void handleShortMessage(ShortMessage message, long timestamp) {
    int status = message.getStatus();
    int note = message.getData1();
    int velocity = message.getData2();
    //    System.out.println("Got message " + status + ", " + note + ", " + velocity + ", " + timestamp);

    if (status == ShortMessage.NOTE_ON || status == ShortMessage.CONTROL_CHANGE) {
      handleButtonPress(note, velocity, timestamp);
      //    } else {
      //      listener.onUnhandledMessageReceived(message, timestamp);
    }
  }

  /**
   * Parses button press messages and notifies the higher-level {@code LaunchpadProtocolListener}
   *
   * @param note The activated note.
   * @param velocity The note velocity.
   * @param timestamp When the note was activated.
   */
  protected void handleButtonPress(int note, int velocity, long timestamp) {
    int position = device.indexToPos((byte) note);
    if (velocity == 0) {
      listener.onButtonReleased(position, timestamp);
    } else {
      listener.onButtonPressed(position, timestamp);
    }
  }

  @Override
  public void close() {}
}
