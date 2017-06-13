package com.salisburyclan.lpviewport.animation;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Timeline;

public abstract class Animation {

  private List<Timeline> timelines;
  // TODO: handle multiple callbacks?
  private Runnable onFinishedCallback;

  public Animation() {
    this.timelines = new ArrayList<>();
  }

  protected void addTimeline(Timeline timeline) {
    timelines.add(timeline);
    timeline.setOnFinished(
        event -> {
          callOnFinishedCallback();
        });
  }

  protected void removeTimeline(Timeline timeline) {
    timelines.remove(timeline);
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

  public void setOnFinished(Runnable callback) {
    this.onFinishedCallback = callback;
  }

  private void callOnFinishedCallback() {
    if (onFinishedCallback != null) {
      onFinishedCallback.run();
    }
  }
}
