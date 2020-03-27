package org.projectcardboard.client.view;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayList {
  int frameWidth;
  int frameHeight;
  int frameDuration;
  double extraX;
  double extraY;
  private final HashMap<String, ArrayList<FramePosition>> lists = new HashMap<>();

  public FramePosition[] getHitFrames() {
    return lists.get("hit").toArray(new FramePosition[1]);
  }

  public FramePosition[] getDeathFrames() {
    return lists.get("death").toArray(new FramePosition[1]);
  }

  public FramePosition[] getBreathingFrames() {
    return lists.get("breathing").toArray(new FramePosition[1]);
  }

  public FramePosition[] getIdleFrames() {
    return lists.get("idle").toArray(new FramePosition[1]);
  }

  public FramePosition[] getAttackFrames() {
    return lists.get("attack").toArray(new FramePosition[1]);
  }

  public FramePosition[] getRunFrames() {
    return lists.get("run").toArray(new FramePosition[1]);
  }

  public int getFrameWidth() {
    return frameWidth;
  }

  public int getFrameHeight() {
    return frameHeight;
  }

  public int getFrameDuration() {
    return frameDuration;
  }

  public double getExtraX() {
    return extraX;
  }

  public double getExtraY() {
    return extraY;
  }

  public HashMap<String, ArrayList<FramePosition>> getLists() {
    return lists;
  }

  public static class FramePosition {
    final double x;
    final double y;

    FramePosition(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }
  }
}


