package com.salisburyclan.lpviewport.animation;

import com.salisburyclan.lpviewport.api.Viewport;
import javafx.animation.Timeline;
import java.util.ArrayList;
import java.util.List;

public abstract class Animation {

  private Viewport viewport;
  private List<Timeline> timelines;
  // TODO: handle multiple callbacks?
  private Runnable onFinishedCallback;

  public Animation(Viewport viewport) {
    this.viewport = viewport;
    this.timelines = new ArrayList<>();
  }

  protected void addTimeline(Timeline timeline) {
    timelines.add(timeline);
    timeline.setOnFinished(event -> {
      callOnFinishedCallback();
    });
  }

  protected Viewport getViewport() {
    return viewport;
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
