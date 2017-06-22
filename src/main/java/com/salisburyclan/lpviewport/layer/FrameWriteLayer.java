package com.salisburyclan.lpviewport.layer;

// Interface for writing to a layer that supports
// frames (draw frame, clear, draw next frame).
public interface FrameWriteLayer extends WriteLayer {
  // Marks the current frame as done.
  // Used only for frame-based animations.
  void nextFrame();
}
