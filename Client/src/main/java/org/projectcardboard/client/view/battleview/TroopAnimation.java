package org.projectcardboard.client.view.battleview;

import static org.projectcardboard.client.view.battleview.CardAnimation.cachedImages;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import Config.Config;
import com.google.gson.Gson;

import org.projectcardboard.client.models.gui.DefaultLabel;
import org.projectcardboard.client.models.gui.ImageLoader;
import org.projectcardboard.client.view.PlayList;
import org.projectcardboard.client.view.PlayList.FramePosition;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TroopAnimation extends Transition {
  private final boolean isPlayer1Troop;
  private final boolean isMyTroop;
  private final double[][] cellsX;
  private final double[][] cellsY;

  private final FramePosition[] attackFramePositions;
  private final FramePosition[] breathingFramePositions;
  private final FramePosition[] deathFramePositions;
  private final FramePosition[] hitFramePositions;
  private final FramePosition[] idleFramePositions;
  private final FramePosition[] runFramePositions;

  private final ImageView imageView;
  private final int frameWidth, frameHeight;

  private boolean frameShowFlag = false;
  private int nextIndex;
  private FramePosition[] currentFramePositions;
  private int currentI, currentJ;

  private ACTION action;
  private boolean shouldRun = false;
  private int nextI, nextJ;
  private boolean shouldDie = false;
  private boolean shouldAttack = false;
  private int attackColumn = 0;
  private boolean shouldHit = false;
  private int hitColumn;
  private boolean isSelected = false;

  private DefaultLabel apLabel;
  private DefaultLabel hpLabel;

  private final Group mapGroup;
  private Group troopGroup;

  TroopAnimation(Group mapGroup, double[][] cellsX, double[][] cellsY, String fileName, int j,
      int i, boolean isPlayer1Troop, boolean isMyTroop, boolean isCustom) throws Exception {
    this.mapGroup = mapGroup;
    this.isPlayer1Troop = isPlayer1Troop;
    this.isMyTroop = isMyTroop;

    // read settings
    String customCardPath = Config.getInstance().getCustomCardSpritesPath().toString() + "/";
    InputStream plistR = isCustom ? new FileInputStream(customCardPath + fileName + ".plist.json")
        : this.getClass().getResourceAsStream("/troopAnimations/" + fileName + ".plist.json");
    PlayList playlist =
        new Gson().fromJson(new InputStreamReader(plistR, StandardCharsets.UTF_8), PlayList.class);
    attackFramePositions = playlist.getAttackFrames();
    breathingFramePositions = playlist.getBreathingFrames();
    deathFramePositions = playlist.getDeathFrames();
    hitFramePositions = playlist.getHitFrames();
    idleFramePositions = playlist.getIdleFrames();
    runFramePositions = playlist.getRunFrames();
    frameWidth = playlist.getFrameWidth();
    frameHeight = playlist.getFrameHeight();
    setCycleDuration(Duration.millis(playlist.getFrameDuration()));

    this.cellsX = cellsX;
    this.cellsY = cellsY;

    currentI = i;
    currentJ = j;
    String path = isCustom ? customCardPath : "/troopAnimations/";
    Image image =
        cachedImages.computeIfAbsent(fileName, key -> ImageLoader.load(path + fileName + ".png"));
    imageView = new ImageView(image);
    imageView.setFitWidth(frameWidth * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setFitHeight(frameHeight * Constants.TROOP_SCALE * Constants.SCALE);
    imageView.setX(-playlist.getExtraX() * Constants.SCALE);
    imageView.setY(-playlist.getExtraY() * Constants.SCALE);
    imageView.setViewport(new Rectangle2D(0, 0, 1, 1));

    troopGroup = new Group();
    troopGroup.setLayoutX(cellsX[j][i]);
    troopGroup.setLayoutY(cellsY[j][i]);
    troopGroup.getChildren().add(imageView);

    this.setCycleCount(INDEFINITE);

    action = ACTION.IDLE;
    setAction(ACTION.BREATHING);

    addApHp();

    mapGroup.getChildren().add(troopGroup);
  }

  private void addApHp() throws Exception {
    ImageView apImage;
    ImageView hpImage;
    apLabel = new DefaultLabel("", Constants.AP_FONT, Color.WHITE, -Constants.SCALE * 29,
        Constants.SCALE * 15);
    hpLabel = new DefaultLabel("", Constants.AP_FONT, Color.WHITE, Constants.SCALE * 14,
        Constants.SCALE * 15);
    if (isMyTroop) {
      apImage =
          new ImageView(new Image(this.getClass().getResourceAsStream("/ui/icon_atk@2x.png")));
      hpImage = new ImageView(new Image(this.getClass().getResourceAsStream("/ui/icon_hp@2x.png")));
    } else {
      apImage =
          new ImageView(new Image(this.getClass().getResourceAsStream("/ui/icon_atk_bw@2x.png")));
      hpImage =
          new ImageView(new Image(this.getClass().getResourceAsStream("/ui/icon_hp_bw@2x.png")));
    }
    apImage.setFitHeight(apImage.getImage().getHeight() * Constants.SCALE * 0.4);
    apImage.setFitWidth(apImage.getImage().getWidth() * Constants.SCALE * 0.4);
    apImage.setX(-Constants.SCALE * 50);
    apImage.setY(Constants.SCALE * 0);
    hpImage.setFitHeight(hpImage.getImage().getHeight() * Constants.SCALE * 0.4);
    hpImage.setFitWidth(hpImage.getImage().getWidth() * Constants.SCALE * 0.4);
    hpImage.setX(-Constants.SCALE * 6);
    hpImage.setY(Constants.SCALE * 0);
    troopGroup.getChildren().addAll(apImage, hpImage, apLabel, hpLabel);
  }

  void updateApHp(int ap, int hp) {
    apLabel.setText(Integer.toString(ap));
    hpLabel.setText(Integer.toString(hp));
  }

  @Override
  protected void interpolate(double v) {
    if (!frameShowFlag && v < 0.5)
      frameShowFlag = true;
    if (frameShowFlag && v > 0.5) {
      imageView.setViewport(new Rectangle2D(currentFramePositions[nextIndex].getX(),
          currentFramePositions[nextIndex].getY(), frameWidth, frameHeight));
      generateNextState();
      frameShowFlag = false;
    }
  }

  private void generateNextState() {
    // has reached to destination
    if (action.equals(ACTION.RUN)
        && Math.abs(troopGroup.getLayoutX() - cellsX[currentJ][currentI]) < 2
        && Math.abs(troopGroup.getLayoutY() - cellsY[currentJ][currentI]) < 2) {// may bug
      generateNextAction();
      return;
    }
    // hasn't reached to last frame
    if (nextIndex != (currentFramePositions.length - 1)) {
      nextIndex++;
      if (action.equals(ACTION.BREATHING) || action.equals(ACTION.IDLE))
        generateNextAction();
      return;
    }
    // has reached to last frame
    // TODO: what about RUN?
    nextIndex = 0;
    switch (action) {
      case HIT:
      case ATTACK:
      case IDLE:
      case BREATHING:
        generateNextAction();
        break;
      case DEATH:
        mapGroup.getChildren().remove(troopGroup);
        break;
    }
  }

  private void generateNextAction() {
    if (shouldRun) {
      shouldRun = false;
      setAction(ACTION.RUN);
      if (nextI > currentI)
        imageView.setScaleX(1);
      if (nextI < currentI)
        imageView.setScaleX(-1);
      KeyValue xValue = new KeyValue(troopGroup.layoutXProperty(), cellsX[nextJ][nextI]);
      KeyValue yValue = new KeyValue(troopGroup.layoutYProperty(), cellsY[nextJ][nextI]);
      KeyFrame keyFrame = new KeyFrame(Duration.millis(
          (Math.abs(currentI - nextI) + Math.abs(currentJ - nextJ)) * Constants.MOVE_TIME_PER_CELL),
          xValue, yValue);
      Timeline timeline = new Timeline(keyFrame);
      timeline.play();
      currentJ = nextJ;
      currentI = nextI;
      return;
    }
    if (shouldHit) {
      shouldHit = false;
      setAction(ACTION.HIT);
      if (hitColumn > currentI)
        imageView.setScaleX(1);
      if (hitColumn < currentI)
        imageView.setScaleX(-1);
      return;
    }
    if (shouldAttack) {
      shouldAttack = false;
      setAction(ACTION.ATTACK);
      if (attackColumn > currentI)
        imageView.setScaleX(1);
      if (attackColumn < currentI)
        imageView.setScaleX(-1);
      return;
    }
    if (shouldDie) {
      shouldDie = false;
      setAction(ACTION.DEATH);
      return;
    }
    if (isSelected)
      setAction(ACTION.IDLE);
    else
      setAction(ACTION.BREATHING);
  }

  private void setAction(ACTION action) {
    if (this.action.equals(action))
      return;
    this.action = action;
    nextIndex = 0;
    this.stop();
    if (isPlayer1Troop)
      imageView.setScaleX(1);
    else
      imageView.setScaleX(-1);
    switch (action) { // Todo check if order matters. E.g. is death before attack the cause of our
                      // no attack animations on death bug (#191) ??
      case BREATHING:
        currentFramePositions = breathingFramePositions;
        break;
      case DEATH:
        currentFramePositions = deathFramePositions;
        break;
      case ATTACK:
        currentFramePositions = attackFramePositions;
        break;
      case IDLE:
        currentFramePositions = idleFramePositions;
        break;
      case RUN:
        currentFramePositions = runFramePositions;
        break;
      case HIT:
        currentFramePositions = hitFramePositions;
        break;
    }
    this.play();
  }

  void kill() {
    shouldDie = true;
  }

  public void attack(int i) {
    shouldAttack = true;
    attackColumn = i;
  }

  public void hit(int i) {
    shouldHit = true;
    hitColumn = i;
  }

  void moveTo(int j, int i) {
    nextI = i;
    nextJ = j;
    shouldRun = true;
  }

  void select() {
    isSelected = true;
  }

  void diSelect() {
    isSelected = false;
  }

  public int getColumn() {
    return currentI;
  }

  public int getRow() {
    return currentJ;
  }

  Group getTroopGroup() {
    return troopGroup;
  }

  enum ACTION {
    ATTACK, BREATHING, DEATH, HIT, IDLE, RUN
  }
}

