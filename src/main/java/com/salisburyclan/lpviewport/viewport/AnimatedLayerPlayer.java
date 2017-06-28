package com.salisburyclan.lpviewport.viewport;

import com.salisburyclan.lpviewport.api.AnimatedLayer;
import com.salisburyclan.lpviewport.api.DecayingAnimation;
import com.salisburyclan.lpviewport.api.Viewport;

// Utilities for playing AnimatedLayers in a RawViewport.
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
