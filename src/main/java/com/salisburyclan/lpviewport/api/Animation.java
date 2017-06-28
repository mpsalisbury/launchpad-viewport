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

  protected void addTimeline(Timeline timeline) {
    timelines.add(timeline);
  }

  public void play() {
    timelines.forEach(Timeline::play);
  }

  public void pause() {
    timelines.forEach(Timeline::pause);
  }

  public void stop() {
    timelines.forEach(Timeline::stop);
  }
}
