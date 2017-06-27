package com.salisburyclan.lpviewport.api;

import com.salisburyclan.lpviewport.viewport.RawViewport;
import com.salisburyclan.lpviewport.viewport.RawViewportViewport;

public class AnimatedLayerPlayer {
  // Plays the given animation as the sole layer in the given rawViewport.
  public static void play(AnimatedLayer animation, RawViewport rawViewport) {
    Viewport viewport = new RawViewportViewport(rawViewport);
    viewport.addLayer(animation);
    animation.play();
  }

  public static void playDecay(AnimatedLayer animation, RawViewport rawViewport) {
    Viewport viewport = new RawViewportViewport(rawViewport);
    viewport.addLayer(new DecayingAnimation(animation));
    animation.play();
  }
}
