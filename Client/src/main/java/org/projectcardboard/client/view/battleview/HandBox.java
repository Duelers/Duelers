package org.projectcardboard.client.view.battleview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.projectcardboard.client.controller.GameController;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.models.game.Player;
import org.projectcardboard.client.models.gui.CardPane;
import org.projectcardboard.client.models.gui.DefaultLabel;
import org.projectcardboard.client.models.gui.DialogBox;
import org.projectcardboard.client.models.gui.DialogContainer;
import org.projectcardboard.client.models.gui.ImageButton;
import org.projectcardboard.client.models.gui.UIConstants;
import org.projectcardboard.client.models.message.OnlineGame;
import org.projectcardboard.client.view.MainMenu;
// import org.projectcardboard.client.models.gui.TurnTimer;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import shared.models.card.Card;
import java.util.Timer;
import java.util.TimerTask;

public class HandBox implements PropertyChangeListener {
  private static final Effect DISABLE_BUTTON_EFFECT = new ColorAdjust(0, -1, 0, 0);
  private final BattleScene battleScene;
  private final Player player;
  private int direction = 1; // direction card/minion is facing. 1 for facing right, -1 for facing
                             // left.
  private final Group handGroup;
  private final Pane[] cards = new Pane[shared.Constants.MAXIMUM_CARD_HAND_SIZE];
  private final StackPane next = new StackPane();
  private int selectedCard = -1;
  private CardPane cardPane = null;
  private final Image cardBack =
      new Image(this.getClass().getResourceAsStream("/ui/card_background@2x.png"));
  private final Image cardBackGlow =
      new Image(this.getClass().getResourceAsStream("/ui/card_background_highlight@2x.png"));
  private final Image nextBack =
      new Image(this.getClass().getResourceAsStream("/ui/replace_background@2x.png"));
  private final Image endTurnImage =
      new Image(this.getClass().getResourceAsStream("/ui/button_end_turn_finished@2x.png"));
  private final Image endTurnImageGlow =
      new Image(this.getClass().getResourceAsStream("/ui/button_end_turn_finished_glow@2x.png"));
  private DefaultLabel endTurnLabel;
  private StackPane endTurnButton;
  private Timer turnTimer = null;
  private int timerCount = 120;
  private DefaultLabel timeRemaining = null;

  HandBox(BattleScene battleScene, Player player) throws Exception {
    this.battleScene = battleScene;
    this.player = player;

    handGroup = new Group();
    handGroup.setLayoutX(Constants.HAND_X);
    handGroup.setLayoutY(Constants.HAND_Y);


    if (player != null) {

      this.direction = player.getPlayerNumber() == 1 ? 1 : -1;

      HBox hBox = new HBox();
      hBox.setLayoutX(200 * Constants.SCALE);
      hBox.setLayoutY(25 * Constants.SCALE);
      hBox.setSpacing(-15 * Constants.SCALE);
      handGroup.getChildren().add(hBox);

      for (int i = 0; i < shared.Constants.MAXIMUM_CARD_HAND_SIZE; i++) {
        cards[i] = new Pane();
        cards[i].setScaleX(direction);
        hBox.getChildren().add(cards[i]);
      }
      updateCards();

      next.setLayoutX(0 * Constants.SCALE);
      next.setLayoutY(0);
      handGroup.getChildren().add(next);
      updateNext();

      addEndTurnButton();
      addGraveYardButton();
      player.addPropertyChangeListener(this);
      battleScene.getGame().addPropertyChangeListener(this);
    }
    this.runTurnTimer();
    addFinishButton();
  }

  private void runTurnTimer() {
    if (this.turnTimer != null) {
      this.turnTimer.cancel();
      this.timerCount = 120;
    }
    this.turnTimer = new Timer();
    this.turnTimer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        Platform.runLater(() -> {
          if (timerCount == 0) {
            turnTimer.cancel();
            if (timeRemaining != null)
              handGroup.getChildren().remove(timeRemaining);
            return;
          }
          if (timeRemaining != null)
            handGroup.getChildren().remove(timeRemaining);
          timeRemaining =
              new DefaultLabel(Integer.toString(timerCount), Constants.END_TURN_FONT, Color.WHITE);
          handGroup.getChildren().add(timeRemaining);
          timerCount--;
        });
      }
    }, 0, 1000);
  }

  /*
   * WARNING: excessive or insufficient calls to updateNext can lead to bad side affects, such as
   * the button being greyed out when it shoudlnt, or the deck size variable showing an incorrect
   * number
   */
  private void updateNext() {
    next.getChildren().clear();
    final ImageView replaceIcon = new ImageView();
    next.getChildren().add(replaceIcon);
    replaceIcon.setFitWidth(Constants.SCREEN_WIDTH * 0.11);
    replaceIcon.setFitHeight(Constants.SCREEN_WIDTH * 0.11);
    replaceIcon.setImage(nextBack);
    boolean canReplace = GameController.getInstance().getAvailableActions().canReplace(player);
    replaceIcon.setEffect(canReplace ? null : DISABLE_BUTTON_EFFECT);
    Text replaceText = new Text("");
    replaceText.setFont(Constants.AP_FONT);
    replaceText.setStyle("-fx-text-base-color: white; -fx-font-size: 15px;");
    replaceText.setFill(Color.WHITE);
    next.getChildren().add(replaceText);
    if (canReplace) {
      replaceText.setText("Replace Available \n Cards Remaining: " + player.getDeckSize());
    } else {
      replaceText.setText("\n Cards Remaining: " + player.getDeckSize());
    }
    next.setOnMouseClicked(mouseEvent -> replaceSelectedCard());
  }

  private void updateCards() {
    for (int i = 0; i < shared.Constants.MAXIMUM_CARD_HAND_SIZE; i++) {
      final int I = i;
      cards[i].getChildren().clear();
      final ImageView imageView = new ImageView();
      cards[i].getChildren().add(imageView);
      imageView.setFitWidth(Constants.SCREEN_WIDTH * 0.085);
      imageView.setFitHeight(Constants.SCREEN_WIDTH * 0.085);

      final CardAnimation cardAnimation;
      if (player.getHand().size() > i) {
        cardAnimation = new CardAnimation(cards[i], player.getHand().get(i),
            imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);
      } else {
        cardAnimation = null;
      }


      if (selectedCard == i && cardAnimation != null) {
        imageView.setImage(cardBackGlow);
        cardAnimation.inActive();
      } else
        imageView.setImage(cardBack);

      if (cardAnimation != null) {
        final Card card = player.getHand().get(I);
        cards[i].setOnMouseEntered(mouseEvent -> {
          if (cardPane != null) {
            handGroup.getChildren().remove(cardPane);
            cardPane = null;
          }
          if (battleScene.isMyTurn()
              && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.in_game_hove);
            cardAnimation.inActive();
            imageView.setImage(cardBackGlow);
          }
          cardPane = new CardPane(card, false, false, null, direction);
          cardPane.setLayoutY(-300 * Constants.SCALE + cards[I].getLayoutY());
          cardPane.setLayoutX(150 * Constants.SCALE + cards[I].getLayoutX());
          handGroup.getChildren().add(cardPane);
        });

        cards[i].setOnMouseExited(mouseEvent -> {
          if (cardPane != null) {
            handGroup.getChildren().remove(cardPane);
            cardPane = null;
          }
          if (selectedCard == I) {
            imageView.setImage(cardBackGlow);
          } else {
            imageView.setImage(cardBack);
            cardAnimation.pause();
          }
        });

        cards[i].setOnMouseClicked(mouseEvent -> {
          if (battleScene.isMyTurn()
              && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
            clickOnCard(I);
          }
        });

        // Grey out cards in hand when its the opponents turn
        boolean haveSufficientManaForCard =
            GameController.getInstance().getAvailableActions().haveSufficientMana(player, card);
        Effect nullOrGrayscale = battleScene.isMyTurn()
            && GameController.getInstance().getAvailableActions().canInsertCard(card)
            && haveSufficientManaForCard ? null : DISABLE_BUTTON_EFFECT;
        cards[i].setEffect(nullOrGrayscale);
      }
    }
  }

  private void addEndTurnButton() {
    try {
      endTurnButton = new StackPane();
      ImageView imageView = new ImageView();
      imageView.setImage(endTurnImage);
      imageView.setFitWidth(endTurnImage.getWidth() * Constants.SCALE * 0.5);
      imageView.setFitHeight(endTurnImage.getHeight() * Constants.SCALE * 0.5);
      endTurnLabel = new DefaultLabel("END TURN", Constants.END_TURN_FONT, Color.WHITE);

      if ((battleScene.getGame().getTurnNumber() + 1) % 2 == battleScene.getMyPlayerNumber() % 2) {
        endTurnLabel.setText("ENEMY TURN");
        endTurnButton.setEffect(DISABLE_BUTTON_EFFECT);
      }
      endTurnButton.setLayoutX(1400 * Constants.SCALE);
      endTurnButton.setLayoutY(5 * Constants.SCALE);
      endTurnButton.getChildren().addAll(imageView, endTurnLabel);
      endTurnButton.setOnMouseExited(mouseEvent -> imageView.setImage(endTurnImage));
      endTurnButton.setOnMouseEntered(mouseEvent -> {
        if (battleScene.isMyTurn()) {
          imageView.setImage(endTurnImageGlow);
        }
      });
      endTurnButton.setOnMouseClicked(mouseEvent -> {
        if (battleScene.isMyTurn()) {
          battleScene.getController().endTurn();
        }
      });
      this.handGroup.getChildren().add(endTurnButton);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addFinishButton() {
    try {
      ImageButton imageButton;
      if (player == null) {
        imageButton = new ImageButton("EXIT", event -> {
          battleScene.getController().exitGameShow(new OnlineGame(battleScene.getGame()));
          new MainMenu().show();
        }, new Image(this.getClass().getResourceAsStream("/ui/button_primary_left@2x.png")),
            new Image(this.getClass().getResourceAsStream("/ui/button_primary_left_glow@2x.png")));
      } else {
        imageButton = new ImageButton("FINISH", event -> battleScene.getController().forceFinish(),
            new Image(this.getClass().getResourceAsStream("/ui/button_primary_left@2x.png")),
            new Image(this.getClass().getResourceAsStream("/ui/button_primary_left_glow@2x.png")));
      }
      imageButton.setLayoutX(1360 * Constants.SCALE);
      imageButton.setLayoutY(110 * Constants.SCALE);

      handGroup.getChildren().add(imageButton);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addGraveYardButton() {
    try {
      ImageButton imageButton = new ImageButton("GRAVEYARD", event -> showGraveyard(),
          new Image(this.getClass().getResourceAsStream("/ui/button_primary_right@2x.png")),
          new Image(this.getClass().getResourceAsStream("/ui/button_primary_right_glow@2x.png")));
      imageButton.setLayoutX(1530 * Constants.SCALE);
      imageButton.setLayoutY(110 * Constants.SCALE);

      handGroup.getChildren().add(imageButton);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showGraveyard() {
    ScrollPane scrollPane = new ScrollPane();
    GridPane cardsPane = new GridPane();
    cardsPane.setVgap(UIConstants.DEFAULT_SPACING * 2);
    cardsPane.setHgap(UIConstants.DEFAULT_SPACING * 2);

    List<Card> graveyard = player.getGraveyard();
    for (int i = 0; i < graveyard.size(); i++) {
      Card card = graveyard.get(i);
      CardPane cardPane = new CardPane(card, false, false, null, direction);
      cardsPane.add(cardPane, i % 3, i / 3);
    }
    scrollPane.setContent(cardsPane);
    scrollPane.setId("background_transparent");
    scrollPane.setMinSize(1600 * UIConstants.SCALE, 1000 * UIConstants.SCALE);
    DialogBox dialogBox = new DialogBox(scrollPane);
    DialogContainer dialogContainer = new DialogContainer(battleScene.root, dialogBox);
    dialogContainer.show();
    dialogBox.makeClosable(dialogContainer);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (player == null)
      return;
    switch (evt.getPropertyName()) {
      case "next":
      case "hand":
        Platform.runLater(this::resetSelection);
        break;
      case "turn":
        if (((int) evt.getNewValue() + 1) % 2 == battleScene.getMyPlayerNumber() % 2) {
          Platform.runLater(() -> {
            endTurnButton.setEffect(DISABLE_BUTTON_EFFECT);
            endTurnLabel.setText("ENEMY TURN");
            this.runTurnTimer();
            updateNext();
          });
        } else {
          Platform.runLater(() -> {
            endTurnButton.setEffect(null);
            endTurnLabel.setText("END TURN");
            this.runTurnTimer();
            updateNext();
          });
        }
        break;
    }
  }

  private void clickOnCard(int i) {
    SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
    if (selectedCard == i) {
      selectedCard = -1;
      battleScene.getMapBox().updateMapColors();
    } else {
      selectedCard = i;
      battleScene.getMapBox().resetSelection();
    }
    updateCards();
  }

  Group getHandGroup() {
    return handGroup;
  }

  Card getSelectedCard() {
    if (selectedCard >= 0)
      return player.getHand().get(selectedCard);
    return null;
  }

  void resetSelection() {
    if (player != null) {
      selectedCard = -1;
      updateCards();
    }
  }

  public void replaceSelectedCard() {
    if (selectedCard != -1) {
      String cardID = player.getHand().get(selectedCard).getCardId();
      battleScene.getController().replaceCard(cardID);
      int currentTimesReplacedThisTurn =
          GameController.getInstance().getAvailableActions().getNumTimesReplacedThisTurn();
      GameController.getInstance().getAvailableActions()
          .setNumTimesReplacedThisTurn(currentTimesReplacedThisTurn + 1);
      clickOnCard(selectedCard);
      updateNext();
    }
  }
}
