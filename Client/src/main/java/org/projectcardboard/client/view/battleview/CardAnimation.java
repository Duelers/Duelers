package org.projectcardboard.client.view.battleview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.projectcardboard.client.models.gui.ImageLoader;

import Config.Config;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import shared.models.card.CardType;
import shared.models.card.ICard;

public class CardAnimation extends Transition {
    static final Map<String, Image> cachedImages = new HashMap<>();
    static final Map<String, Playlist> cachedPlaylists = new HashMap<>();
    private final Group group;
    private final ImageView imageView;
    private FramePosition[] activeFramePositions;
    private FramePosition[] inActiveFramePositions;
    private int frameWidth, frameHeight;
    private double extraX, extraY;

    private boolean flag = false;
    private int nextIndex;
    private ACTION action;
    private FramePosition[] currentFramePositions;

    private final double x;
    private final double y;

    public CardAnimation(Group group, ICard card, double y, double x) {
        this.group = group;
        this.x = x;
        this.y = y;
        //file settings
        Playlist playlist;
        Image image;
        if (CardType.SPELL.equals(card.getType())) {
            String path = card.getIsCustom() ? Config.getInstance().getPathToCustomCardSprites() : "/icons/";
            image = cachedImages.computeIfAbsent(card.getSpriteName(), key -> ImageLoader.load(path + card.getSpriteName() + ".png"));
            playlist = cachedPlaylists.computeIfAbsent(card.getSpriteName(), key -> {
                try {
                    InputStream plistR = this.getClass().getResourceAsStream(path + card.getSpriteName() + ".plist.json");
                    if (plistR == null) { throw new FileNotFoundException(); }
                    return new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8), Playlist.class);
                } catch (FileNotFoundException e) {
                    try{
                        FileInputStream inputStream = new FileInputStream( path + card.getSpriteName() + ".plist.json" );
                        return new Gson().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), Playlist.class);
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                        return new Playlist();
                    }
                }
            });
            activeFramePositions = playlist.getLists().get("active").toArray(new FramePosition[1]);
            inActiveFramePositions = playlist.getLists().get("inactive").toArray(new FramePosition[1]);
            extraX = 38 * Constants.SCALE;
            extraY = 31 * Constants.SCALE;
        } else {
            String path = card.getIsCustom() ? Config.getInstance().getPathToCustomCardSprites() : "/troopAnimations/";
            image = cachedImages.computeIfAbsent(card.getSpriteName(), key -> ImageLoader.load( path + card.getSpriteName() + ".png"));
            playlist = cachedPlaylists.computeIfAbsent(card.getSpriteName(), key -> {
                try {
                    InputStream plistR = this.getClass().getResourceAsStream( path + card.getSpriteName() + ".plist.json");
                    if (plistR == null) { throw new FileNotFoundException(); }
                    return new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8), Playlist.class);
                } catch (FileNotFoundException e) {
                    try{
                        FileInputStream inputStream = new FileInputStream( path + card.getSpriteName() + ".plist.json" );
                        return new Gson().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), Playlist.class);
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                        return new Playlist();
                    }
                }
            });
            activeFramePositions = playlist.getLists().get("idle").toArray(new FramePosition[1]);
            inActiveFramePositions = activeFramePositions;
            extraX = playlist.extraX * Constants.SCALE;
            extraY = (playlist.extraY - 20) * Constants.SCALE;
        }
      });
      activeFramePositions = playlist.getLists().get("active").toArray(new FramePosition[1]);
      inActiveFramePositions = playlist.getLists().get("inactive").toArray(new FramePosition[1]);
      extraX = 38 * Constants.SCALE;
      extraY = 31 * Constants.SCALE;
    } else {
      image = cachedImages.computeIfAbsent(card.getSpriteName(),
          key -> ImageLoader.load("/troopAnimations/" + card.getSpriteName() + ".png"));
      playlist = cachedPlaylists.computeIfAbsent(card.getSpriteName(), key -> {
        try {
          InputStream plistR = this.getClass()
              .getResourceAsStream("/troopAnimations/" + card.getSpriteName() + ".plist.json");
          if (plistR == null) {
            throw new FileNotFoundException();
          }
          return new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8),
              Playlist.class);
        } catch (FileNotFoundException e) {
          return new Playlist();
        }
      });
      activeFramePositions = playlist.getLists().get("idle").toArray(new FramePosition[1]);
      inActiveFramePositions = activeFramePositions;
      extraX = playlist.extraX * Constants.SCALE;
      extraY = (playlist.extraY - 20) * Constants.SCALE;
    }

    frameWidth = playlist.frameWidth;
    frameHeight = playlist.frameHeight;
    setCycleDuration(Duration.millis(playlist.frameDuration));


    imageView = new ImageView(image);

    imageView.setFitWidth(frameWidth * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setFitHeight(frameHeight * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setX(x - extraX);
    imageView.setY(y - extraY);

    imageView.setViewport(new Rectangle2D(0, 0, 1, 1));
    this.group.getChildren().add(imageView);


    this.setCycleCount(INDEFINITE);
    setAction(ACTION.STOPPED);
  }

  public CardAnimation(Pane pane, ICard card, double y, double x) {// TODO:Change
    this(new Group(), card, y, x);
    pane.getChildren().add(group);
  }

  @Override
  protected void interpolate(double v) {
    if (!flag && v < 0.5)
      flag = true;
    if (flag && v > 0.5) {
      imageView.setViewport(new Rectangle2D(currentFramePositions[nextIndex].x,
          currentFramePositions[nextIndex].y, frameWidth, frameHeight));
      generateNextState();
      flag = false;
    }
  }

  private void generateNextState() {
    if (action.equals(ACTION.STOPPED)) {
      nextIndex = 0;
      return;
    }
    if (nextIndex != (currentFramePositions.length - 1)) {
      nextIndex++;
      return;
    }
    // has reached to last frame
    // TODO: what about STOPPED?
    switch (action) {
      case ACTIVE:
        group.getChildren().remove(imageView);
        break;
      case IN_ACTIVE:
        nextIndex = 0;
        break;
    }
  }

  private void setAction(ACTION action) {
    if (action.equals(this.action)) {
      return;
    }
    this.action = action;
    nextIndex = 0;
    this.stop();
    switch (action) {
      case ACTIVE:
        currentFramePositions = activeFramePositions;
        break;
      case IN_ACTIVE:
      case STOPPED:
        currentFramePositions = inActiveFramePositions;
        break;
    }
    this.play();
  }

  public void active() {
    setAction(ACTION.ACTIVE);
  }

  public void inActive() {
    setAction(ACTION.IN_ACTIVE);
  }

  public void pause() {
    setAction(ACTION.STOPPED);
  }

  public ImageView getImageView() {
    return imageView;
  }

  public void setSprite(ICard card) {
    this.stop();
    Playlist playlist;
    Image image;
    if (card.getType().equals(CardType.SPELL)) {
      image = cachedImages.computeIfAbsent(card.getSpriteName(),
          key -> ImageLoader.load("/icons/" + card.getSpriteName() + ".png"));
      playlist = cachedPlaylists.computeIfAbsent(card.getSpriteName(), key -> {
        try {
          InputStream plistR =
              this.getClass().getResourceAsStream("/icons/" + card.getSpriteName() + ".plist.json");
          if (plistR == null) {
            throw new FileNotFoundException();
          }
          return new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8),
              Playlist.class);
        } catch (FileNotFoundException e) {
          return new Playlist();
        }
      });
      activeFramePositions = playlist.getLists().get("active").toArray(new FramePosition[1]);
      inActiveFramePositions = playlist.getLists().get("inactive").toArray(new FramePosition[1]);
      extraX = 38 * Constants.SCALE;
      extraY = 31 * Constants.SCALE;
    } else {
      image = cachedImages.computeIfAbsent(card.getSpriteName(),
          key -> ImageLoader.load("/troopAnimations/" + card.getSpriteName() + ".png"));
      playlist = cachedPlaylists.computeIfAbsent(card.getSpriteName(), key -> {
        try {
          InputStream plistR = this.getClass()
              .getResourceAsStream("/troopAnimations/" + card.getSpriteName() + ".plist.json");
          if (plistR == null) {
            throw new FileNotFoundException();
          }
          return new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8),
              Playlist.class);
        } catch (FileNotFoundException e) {
          return new Playlist();
        }
      });
      activeFramePositions = playlist.getLists().get("idle").toArray(new FramePosition[1]);
      inActiveFramePositions = activeFramePositions;
      extraX = playlist.extraX * Constants.SCALE;
      extraY = (playlist.extraY - 20) * Constants.SCALE;
    }

    frameWidth = playlist.frameWidth;
    frameHeight = playlist.frameHeight;

    imageView.setImage(image);

    imageView.setFitWidth(frameWidth * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setFitHeight(frameHeight * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setX(x - extraX);
    imageView.setY(y - extraY);

    imageView.setViewport(new Rectangle2D(0, 0, 1, 1));
    setAction(ACTION.IN_ACTIVE);
    setAction(ACTION.STOPPED);
  }

  enum ACTION {
    ACTIVE, IN_ACTIVE, STOPPED
  }

  class Playlist {
    int frameWidth;
    int frameHeight;
    int frameDuration;
    double extraX;
    double extraY;
    private final HashMap<String, ArrayList<FramePosition>> lists = new HashMap<>();

    public HashMap<String, ArrayList<FramePosition>> getLists() {
      return lists;
    }
  }

  static class FramePosition {
    final double x;
    final double y;

    FramePosition(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }
}
