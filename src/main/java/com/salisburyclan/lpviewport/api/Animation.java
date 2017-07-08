package com.salisburyclan.lpviewport.api;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Timeline;

// An object supporting play/pause/stop.
// Subclasses should add their animation Timelines via addTimeline().
public abstract class Animation {

  private List<Timeline> timelines;

  public Animation() {
    this.timelines = new ArrayList<>();
  }

  /**
   * Adds the given Timeline to this Animation.
   * @param timeline the Timeline to add
   */
  protected void addTimeline(Timeline timeline) {
    timelines.add(timeline);
  }

  /** Plays all Timelines in this Animation. */
  public void play() {
    timelines.forEach(Timeline::play);
  }

  /** Pauses all Timelines in this Animation. */
  public void pause() {
    timelines.forEach(Timeline::pause);
  }

  /** Stops all Timelines in this Animation. */
  public void stop() {
    timelines.forEach(Timeline::stop);
  }
}
