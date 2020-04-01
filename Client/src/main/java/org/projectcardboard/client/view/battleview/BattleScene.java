package org.projectcardboard.client.view.battleview;

import org.projectcardboard.client.controller.GraphicalUserInterface;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import org.projectcardboard.client.models.game.Game;
import org.projectcardboard.client.models.game.Player;
import org.projectcardboard.client.models.game.GameActions;
import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;
import org.projectcardboard.client.view.Show;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.loos_match;
import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.victory_match;

public class BattleScene extends Show {
  private static final String WINNER_SPRITE_NAME = "fx_winner";
  private static final Media backgroundMusic =
      new Media(BattleScene.class.getResource("/music/music_battlemap_vetruv.m4a").toString());
  private static final Map<SpellType, String> spellSpriteNames = new HashMap<>();
  private final GameActions controller;
  private final Game game;
  private final MapBox mapBox;
  private final HandBox handBox;
  private final PlayerBox playerBox;
  private final int myPlayerNumber;
  private Player myPlayer;
  private Player oppPlayer;

  static {
    spellSpriteNames.put(SpellType.ATTACK, "fx_f4_shadownova");
    spellSpriteNames.put(SpellType.PUT, "fx_f2_phoenixfire");
    spellSpriteNames.put(SpellType.DEATH, "fx_f4_deathfire_crescendo");
    spellSpriteNames.put(SpellType.DEFEND, "fx_f2_bbs_spellsword");
    spellSpriteNames.put(SpellType.CONTINUOUS, "fx_f3_sandportal");
    spellSpriteNames.put(SpellType.DEFAULT, "fx_f2_eightgates_teallotus");
  }

  public BattleScene(GameActions controller, Game game, int myPlayerNumber, String mapName)
      throws Exception {
    this.controller = controller;
    this.game = game;
    this.myPlayerNumber = myPlayerNumber;
    addBackGround(mapName);
    if (myPlayerNumber == 1) {
      myPlayer = game.getPlayerOne();
      oppPlayer = game.getPlayerTwo();
    } else if (myPlayerNumber == 2) {
      myPlayer = game.getPlayerTwo();
      oppPlayer = game.getPlayerOne();
    }

    handBox = new HandBox(this, myPlayer);
    playerBox = new PlayerBox(this, game);
    mapBox = new MapBox(this, game.getGameMap());

    root.getChildren().addAll(mapBox.getMapGroup(), playerBox.getGroup(), handBox.getHandGroup());
  }

  private void addBackGround(String address) {
    try {
      InputStream imageR = this.getClass().getResourceAsStream("/backGrounds/" + address + ".png");
      if (imageR == null) {
        throw new FileNotFoundException();
      }
      Image image = new Image(imageR);
      ImageView backGround = new ImageView(image);
      backGround.setFitWidth(Constants.SCREEN_WIDTH);
      backGround.setFitHeight(Constants.SCREEN_HEIGHT);
      root.getChildren().add(backGround);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void attack(String cardId, String defender) {
    mapBox.showAttack(cardId, defender);
    new Thread(() -> {
      try {
        Thread.sleep(200);
        defend(defender, cardId);
      } catch (InterruptedException ignored) {
      }
    }).start();
  }

  void defend(String defender, String attacker) {
    mapBox.showDefend(defender, attacker);
  }

  public void spell(String fxName, Cell cell) {
    mapBox.showSpell(fxName, cell.getRow(), cell.getColumn());
  }

  @Override
  public void show() {
    super.show();
    GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
  }

  @Override
  public void showError(String message, String buttonText,
      EventHandler<? super MouseEvent> event) {}

  MapBox getMapBox() {
    return mapBox;
  }

  HandBox getHandBox() {
    return handBox;
  }

  PlayerBox getPlayerBox() {
    return playerBox;
  }

  public GameActions getController() {
    return controller;
  }

  public int getMyPlayerNumber() {
    return myPlayerNumber;
  }

  boolean isMyTurn() {
    return (game.getTurnNumber() % 2 == 1 && myPlayerNumber == 1)
        || (game.getTurnNumber() % 2 == 0 && myPlayerNumber == 2);
  }

  public Game getGame() {
    return game;
  }

  public void showOpponentMessage(String text) {
    playerBox.showMessage(oppPlayer, text);
  }

  public void showMyMessage(String text) {
    playerBox.showMessage(myPlayer, text);
  }

  public String getMyUserName() {
    return myPlayer.getUserName();
  }

  public String getOppUserName() {
    return oppPlayer.getUserName();
  }

  public void finish(boolean amIWinner) {
    if (myPlayerNumber == -1) {
      return;
    }
    if (amIWinner) {
      SoundEffectPlayer.getInstance().playSound(victory_match);
      if (myPlayer.getHero() == null) {
        mapBox.showSpell(WINNER_SPRITE_NAME, myPlayer.getTroops().get(0).getCell().getRow(),
            myPlayer.getTroops().get(0).getCell().getColumn());
      } else {
        mapBox.showSpell(WINNER_SPRITE_NAME, myPlayer.getHero().getCell().getRow(),
            myPlayer.getHero().getCell().getColumn());
      }
    } else {
      SoundEffectPlayer.getInstance().playSound(loos_match);
      if (oppPlayer.getHero() == null) {
        mapBox.showSpell(WINNER_SPRITE_NAME, oppPlayer.getTroops().get(0).getCell().getRow(),
            oppPlayer.getTroops().get(0).getCell().getColumn());
      } else {
        mapBox.showSpell(WINNER_SPRITE_NAME, oppPlayer.getHero().getCell().getRow(),
            oppPlayer.getHero().getCell().getColumn());
      }
    }
  }

  private enum SpellType {
    ATTACK, PUT, DEATH, CONTINUOUS, DEFEND, DEFAULT
  }
}
