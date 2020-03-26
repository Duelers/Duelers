package org.projectcardboard.client.controller;

import org.projectcardboard.client.models.game.Game;
import org.projectcardboard.client.models.exceptions.InputException;
import org.projectcardboard.client.models.game.GameActions;
import org.projectcardboard.client.models.game.availableactions.AvailableActions;
import org.projectcardboard.client.models.message.CardAnimation;
import org.projectcardboard.client.models.message.GameAnimations;
import org.projectcardboard.client.models.message.GameCopyMessage;
import org.projectcardboard.client.models.message.Message;
import org.projectcardboard.client.models.message.OnlineGame;
import org.projectcardboard.client.view.battleview.BattleScene;

import Config.Config;
import javafx.application.Platform;
import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.game.Troop;
import shared.models.game.map.Cell;


public class GameController implements GameActions {
  private static GameController ourInstance;
  BattleScene battleScene;
  private Game currentGame;
  private final AvailableActions availableActions = constructAvailableActions();


  private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

  private GameController() {}

  public static GameController getInstance() {
    if (ourInstance == null) {
      ourInstance = new GameController();
    }
    return ourInstance;
  }

  public void calculateAvailableActions() {
    availableActions.calculate(currentGame);
  }

  public AvailableActions getAvailableActions() {
    return availableActions;
  }

  public Game getCurrentGame() {
    return currentGame;
  }

  public void setCurrentGame(GameCopyMessage gameCopyMessage) {
    this.currentGame = gameCopyMessage.getGame();
    this.currentGame.getPlayerOne()
        .setTroops(gameCopyMessage.getGame().getGameMap().getTroopsBelongingToPlayer(1));
    this.currentGame.getPlayerTwo()
        .setTroops(gameCopyMessage.getGame().getGameMap().getTroopsBelongingToPlayer(2));
    this.getCurrentGame().getPlayerOne().setDeckSize(gameCopyMessage.getP1StartingDeckSize());
    this.getCurrentGame().getPlayerTwo().setDeckSize(gameCopyMessage.getP2StartingDeckSize());
    int playerNumber = getPlayerNumber(gameCopyMessage.getGame());
    Platform.runLater(() -> {
      try {
        battleScene = new BattleScene(this, gameCopyMessage.getGame(), playerNumber,
            "battlemap6_middleground@2x");
        battleScene.show();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

  }

  private int getPlayerNumber(Game currentGame) {
    int playerNumber = -1;
    if (currentGame.getPlayerOne().getUserName()
        .equals(Client.getInstance().getAccount().getUsername())) {
      playerNumber = 1;
    }
    if (currentGame.getPlayerTwo().getUserName()
        .equals(Client.getInstance().getAccount().getUsername())) {
      playerNumber = 2;
    }
    return playerNumber;
  }

  @Override
  public void attack(Troop attackerTroop, Troop defenderTroop) {
    try {
      if (!attackerTroop.canAttack() || attackerTroop.getCurrentAp() == 0)
        throw new InputException("Error: troop cannot attack and/or current 'ap' is 0.");
      if (attackerTroop.getCard().getAttackType().equals(AttackType.MELEE)) {
        if (!attackerTroop.getCell().isNearbyCell(defenderTroop.getCell())) {
          throw new InputException("Error: target is outside of MELEE range");
        }
      } else if (attackerTroop.getCard().getAttackType().equals(AttackType.RANGED)) {
        if (attackerTroop.getCell().isNearbyCell(defenderTroop.getCell()) || attackerTroop.getCell()
            .manhattanDistance(defenderTroop.getCell()) > attackerTroop.getCard().getRange()) {
          throw new InputException(String.format("Error: target is outside of range (%d)",
              attackerTroop.getCard().getRange()));
        }
      } else { // HYBRID
        if (attackerTroop.getCell().manhattanDistance(defenderTroop.getCell()) > attackerTroop
            .getCard().getRange()) {
          throw new InputException(String.format("Error: target is outside of range (%d)",
              attackerTroop.getCard().getRange()));
        }
      }

      Message message = Message.makeAttackMessage(SERVER_NAME, attackerTroop.getCard().getCardId(),
          defenderTroop.getCard().getCardId());
      Client.getInstance().addToSendingMessagesAndSend(message);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }


  @Override
  public void move(Troop selectedTroop, int row, int column) {
    try {
      Cell target = new Cell(row, column);
      if (!selectedTroop.canMove()) {
        throw new InputException("troop can not move");
      }

      if (currentGame.getGameMap().getTroopAtLocation(new Cell(row, column)) != null) {
        throw new InputException("cell is not empty");
      }

      if (!selectedTroop.getCard().getDescription().contains("Flying")) {
        if (selectedTroop.getCell().manhattanDistance(new Cell(row, column)) > 2) {
          throw new InputException("too far to go");
        }
      }

      Message message =
          Message.makeMoveTroopMessage(SERVER_NAME, selectedTroop.getCard().getCardId(), target);
      Client.getInstance().addToSendingMessagesAndSend(message);
    } catch (InputException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void endTurn() {
    Client.getInstance().addToSendingMessagesAndSend(Message.makeEndTurnMessage(SERVER_NAME));
  }

  @Override
  public void replaceCard(String cardID) {
    Client.getInstance()
        .addToSendingMessagesAndSend(Message.makeNewReplaceCardMessage(SERVER_NAME, cardID));
  }

  public void forceFinish() {
    Client.getInstance()
        .addToSendingMessagesAndSend(Message.makeForceFinishGameMessage(SERVER_NAME));
  }

  @Override
  public void insert(Card card, int row, int column) {
    if (validatePositionForInsert(card, row, column))
      Client.getInstance().addToSendingMessagesAndSend(
          Message.makeInsertMessage(SERVER_NAME, card.getCardId(), new Cell(row, column)));
  }

  private boolean validatePositionForInsert(Card card, int row, int column) {
    return (card.getType().equals(CardType.SPELL))
        || (currentGame.getGameMap().getTroopAtLocation(new Cell(row, column)) == null);
  }


  @Override
  public void exitGameShow(OnlineGame onlineGame) {
    Client.getInstance()
        .addToSendingMessagesAndSend(Message.makeStopShowGameMessage(SERVER_NAME, onlineGame));
  }

  public void showAnimation(GameAnimations gameAnimations) {
    new Thread(() -> {
      gameAnimations.getSpellAnimations().forEach(spellAnimation -> spellAnimation.getCells()
          .forEach(position -> battleScene.spell(spellAnimation.getFxName(), position)));
      for (CardAnimation cardAnimation : gameAnimations.getAttacks()) {
        battleScene.attack(cardAnimation.getAttacker(), cardAnimation.getDefender());
      }
      try {
        Thread.sleep(2000);
      } catch (InterruptedException ignored) {
      }
      for (CardAnimation cardAnimation : gameAnimations.getCounterAttacks()) {
        battleScene.attack(cardAnimation.getAttacker(), cardAnimation.getDefender());
      }
    }).start();
  }

  public void sendChat(String text) {
    Client.getInstance().addToSendingMessagesAndSend(Message.makeChatMessage(SERVER_NAME,
        battleScene.getMyUserName(), battleScene.getOppUserName(), text));
  }

  private AvailableActions constructAvailableActions() {
    AvailableActions availableActions;
    try {
      availableActions = new AvailableActions();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      availableActions = null;
    }
    return availableActions;
  }
}
