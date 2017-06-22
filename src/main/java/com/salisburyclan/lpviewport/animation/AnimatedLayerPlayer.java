package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport;

public class AnimatedLayerPlayer {
  // Plays the given animation as the sole layer in the given rawViewport.
  public static void play(AnimatedLayer animation, RawViewport rawViewport) {
    Viewport viewport = new Viewport(rawViewport);
    viewport.addLayer(animation);
    animation.play();
  }
}
