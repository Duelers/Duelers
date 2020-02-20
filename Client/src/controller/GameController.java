package controller;

import javafx.application.Platform;
import models.card.AttackType;
import models.card.CardType;
import models.comperessedData.CompressedCard;
import models.comperessedData.CompressedGame;
import models.comperessedData.CompressedTroop;
import models.exceptions.InputException;
import models.game.GameActions;
import models.game.availableActions.AvailableActions;
import models.game.map.Position;
import message.*;
import view.BattleView.BattleScene;


import static models.Constants.SERVER_NAME;


public class GameController implements GameActions {
    private static GameController ourInstance;
    BattleScene battleScene;
    private CompressedGame currentGame;
    private AvailableActions availableActions = new AvailableActions();

    private GameController() {
    }

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

    public CompressedGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(CompressedGame currentGame) {
        this.currentGame = currentGame;
        currentGame.getPlayerOne().setTroops(currentGame.getGameMap().getPlayerTroop(1));
        currentGame.getPlayerTwo().setTroops(currentGame.getGameMap().getPlayerTroop(2));
        int playerNumber = getPlayerNumber(currentGame);
        Platform.runLater(() -> {
            try {
                battleScene = new BattleScene(this, currentGame, playerNumber, "battlemap6_middleground@2x");
                battleScene.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private int getPlayerNumber(CompressedGame currentGame) {
        int playerNumber = -1;
        if (currentGame.getPlayerOne().getUserName().equals(Client.getInstance().getAccount().getUsername())) {
            playerNumber = 1;
        }
        if (currentGame.getPlayerTwo().getUserName().equals(Client.getInstance().getAccount().getUsername())) {
            playerNumber = 2;
        }
        return playerNumber;
    }

    @Override
    public void attack(CompressedTroop attackerTroop, CompressedTroop defenderTroop) {
        try {
            if (!attackerTroop.canAttack())
                throw new InputException("you can not attack");
            if (attackerTroop.getCard().getAttackType() == AttackType.MELEE) {
                if (!attackerTroop.getPosition().isNextTo(defenderTroop.getPosition())) {
                    throw new InputException("you can not attack to this target");
                }
            } else if (attackerTroop.getCard().getAttackType() == AttackType.RANGED) {
                if (attackerTroop.getPosition().isNextTo(defenderTroop.getPosition()) ||
                        attackerTroop.getPosition().manhattanDistance(defenderTroop.getPosition()) > attackerTroop.getCard().getRange()) {
                    throw new InputException("you can not attack to this target");
                }
            } else { // HYBRID
                if (attackerTroop.getPosition().manhattanDistance(defenderTroop.getPosition()) > attackerTroop.getCard().getRange()) {
                    throw new InputException("you can not attack to this target");
                }
            }

            Message message = Message.makeAttackMessage(SERVER_NAME, attackerTroop.getCard().getCardId(), defenderTroop.getCard().getCardId());
            Client.getInstance().addToSendingMessagesAndSend(message);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void move(CompressedTroop selectedTroop, int row, int column) {
        try {
            Position target = new Position(row, column);
            if (!selectedTroop.canMove()) {
                throw new InputException("troop can not move");
            }

            if (currentGame.getGameMap().getTroop(new Position(row, column)) != null) {
                throw new InputException("cell is not empty");
            }

            if (selectedTroop.getPosition().manhattanDistance(row, column) > 2) {
                throw new InputException("too far to go");
            }

            Message message = Message.makeMoveTroopMessage(SERVER_NAME, selectedTroop.getCard().getCardId(), target);
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
    public void setNewNextCard() {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeSetNewNextCardMessage(SERVER_NAME));
    }

    @Override
    public void replaceCard(String cardID) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeNewReplaceCardMessage(SERVER_NAME, cardID));
    }

    public void forceFinish() {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeForceFinishGameMessage(SERVER_NAME));
    }

    @Override
    public void insert(CompressedCard card, int row, int column) {
        if (validatePositionForInsert(card, row, column))
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeInsertMessage(SERVER_NAME, card.getCardId(), new Position(row, column)));
    }

    private boolean validatePositionForInsert(CompressedCard card, int row, int column) {
        return (card.getType() == CardType.SPELL) || (currentGame.getGameMap().getTroop(new Position(row, column)) == null);
    }


    @Override
    public void exitGameShow(OnlineGame onlineGame) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeStopShowGameMessage(SERVER_NAME, onlineGame));
    }

    public void showAnimation(GameAnimations gameAnimations) {
        new Thread(() -> {
            gameAnimations.getSpellAnimations().forEach(
                    spellAnimation -> spellAnimation.getPositions().forEach(
                            position -> battleScene.spell(spellAnimation.getAvailabilityType(), position)
                    )
            );
            for (CardAnimation cardAnimation :
                    gameAnimations.getAttacks()) {
                battleScene.attack(cardAnimation.getAttacker(), cardAnimation.getDefender());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            for (CardAnimation cardAnimation :
                    gameAnimations.getCounterAttacks()) {
                battleScene.attack(cardAnimation.getAttacker(), cardAnimation.getDefender());
            }
        }).start();
    }

    public void sendChat(String text) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeChatMessage(SERVER_NAME,
                        battleScene.getMyUserName(), battleScene.getOppUserName(), text));
    }
}
