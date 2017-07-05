package com.salisburyclan.lpviewport.apps;

import com.salisburyclan.lpviewport.animation.CircleExplode;
import com.salisburyclan.lpviewport.animation.Explode;
import com.salisburyclan.lpviewport.animation.Spark;
import com.salisburyclan.lpviewport.api.Button1Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.DecayingAnimation;
import com.salisburyclan.lpviewport.api.FramedAnimation;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.SubView;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;

public class SelectAnimationApp extends LaunchpadApplication {

  private enum AnimationType {
    SPARK,
    EXPLODE,
    CIRCLE_EXPLODE
  }

  private AnimationType chosenAnimation = AnimationType.SPARK;

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport animationViewport) {
    // Make a viewport for select buttons and one for the animation buttons
    Range2 animationExtent = animationViewport.getExtent();

    // Create the Activator Viewport
    // All buttons except the right column
    Range2 activatorExtent = animationExtent.inset(0, 0, 1, 0);
    Viewport activatorViewport = SubView.getSubViewport(animationViewport, activatorExtent);

    // Create the Selector Viewport
    // All buttons in the right column
    Range2 selectorExtent =
        Range2.create(animationExtent.xRange().high(), animationExtent.yRange());
    Viewport1 selectorViewport = SubView.getSubViewport1(animationViewport, selectorExtent);

    activatorViewport.addListener(
        new Button2Listener() {
          @Override
          public void onButtonPressed(Point p) {
            FramedAnimation animation = createSelectedAnimation(animationExtent, p);
            animationViewport.addLayer(new DecayingAnimation(animation));
            animation.play();
          }
        });

    selectorViewport.addListener(
        new Button1Listener() {
          @Override
          public void onButtonPressed(int p) {
            chooseAnimation(p);
          }
        });
  }

  private void chooseAnimation(int p) {
    switch (p) {
      case 0:
        chosenAnimation = AnimationType.SPARK;
        break;
      case 1:
        chosenAnimation = AnimationType.EXPLODE;
        break;
      case 2:
        chosenAnimation = AnimationType.CIRCLE_EXPLODE;
        break;
      default:
    }
  }

  private FramedAnimation createSelectedAnimation(Range2 extent, Point p) {
    switch (chosenAnimation) {
      case SPARK:
        return new Spark(extent, p, getBaseColor(p));
      case EXPLODE:
        return new Explode(extent, p, getBaseColor(p));
      case CIRCLE_EXPLODE:
        return new CircleExplode(extent, p, getBaseColor(p));
      default:
        throw new IllegalStateException("Invalid Chosen Animation");
    }
  }

  // Returns a color for the given index.
  private Color getBaseColor(Point p) {
    final Color colors[] = {
      Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE,
    };
    int index = p.x() + p.y();
    index = index % colors.length;
    if (index < 0) index += colors.length;
    return colors[index];
  }
}
