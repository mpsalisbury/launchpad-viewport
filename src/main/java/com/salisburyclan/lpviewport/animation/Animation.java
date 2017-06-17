package com.salisburyclan.lpviewport.animation;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Timeline;

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
