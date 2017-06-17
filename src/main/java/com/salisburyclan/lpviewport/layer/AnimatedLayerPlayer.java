package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport;

public class AnimatedLayerPlayer {
  public static void play(AnimatedLayer animation, RawViewport rawViewport) {
    Viewport viewport = new Viewport(rawViewport);
    viewport.addLayer(animation);
    animation.play();
  }
}
