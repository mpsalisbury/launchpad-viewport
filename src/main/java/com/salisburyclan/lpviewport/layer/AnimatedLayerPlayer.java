package com.salisburyclan.lpviewport.layer;

import com.salisburyclan.lpviewport.animation.FramedAnimation;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.api.Viewport;

// TODO rename class if this stays.
public class AnimatedLayerPlayer {
  public static void play(FramedAnimation animation, RawViewport rawViewport) {
    Viewport viewport = new Viewport(rawViewport);
    viewport.addLayer(animation);
    animation.play();
  }
}
