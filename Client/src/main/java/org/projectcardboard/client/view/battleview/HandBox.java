package org.projectcardboard.client.view.battleview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.util.List;

import org.projectcardboard.client.controller.GameController;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.models.compresseddata.CompressedPlayer;
import org.projectcardboard.client.models.game.availableactions.AvailableActions;
import org.projectcardboard.client.models.gui.CardPane;
import org.projectcardboard.client.models.gui.DefaultLabel;
import org.projectcardboard.client.models.gui.DialogBox;
import org.projectcardboard.client.models.gui.DialogContainer;
import org.projectcardboard.client.models.gui.ImageButton;
import org.projectcardboard.client.models.gui.UIConstants;
import org.projectcardboard.client.models.message.OnlineGame;
import org.projectcardboard.client.view.MainMenu;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import shared.models.card.Card;

public class HandBox implements PropertyChangeListener {
    private static final Effect DISABLE_BUTTON_EFFECT = new ColorAdjust(0, -1, 0, 0);
    private final BattleScene battleScene;
    private final CompressedPlayer player;
    private final Group handGroup;
    private final Pane[] cards = new Pane[Constants.MAXIMUM_CARD_HAND_SIZE];

	private CardAnimation[] loadedCards = new CardAnimation[Constants.MAXIMUM_CARD_HAND_SIZE];
	private ImageView[] unitSlots = new ImageView[Constants.MAXIMUM_CARD_HAND_SIZE];

	private final Pane next = new Pane();
    private int selectedCard = -1;
    private CardPane cardPane = null;
    private final Image cardBack = new Image(new FileInputStream("Client/src/main/resources/ui/card_background@2x.png"));
    private final Image cardBackGlow = new Image(new FileInputStream("Client/src/main/resources/ui/card_background_highlight@2x.png"));
    private final Image nextBack = new Image(new FileInputStream("Client/src/main/resources/ui/replace_background@2x.png"));
    private final Image endTurnImage = new Image(new FileInputStream("Client/src/main/resources/ui/button_end_turn_finished@2x.png"));
    private final Image endTurnImageGlow = new Image(new FileInputStream("Client/src/main/resources/ui/button_end_turn_finished_glow@2x.png"));
    private DefaultLabel endTurnLabel;
    private StackPane endTurnButton;


    HandBox(BattleScene battleScene, CompressedPlayer player) throws Exception {
        this.battleScene = battleScene;
        this.player = player;
        handGroup = new Group();
        handGroup.setLayoutX(Constants.HAND_X);
        handGroup.setLayoutY(Constants.HAND_Y);


        if (player != null) {
            HBox hBox = new HBox();
            hBox.setLayoutX(200 * Constants.SCALE);
            hBox.setLayoutY(25 * Constants.SCALE);
            hBox.setSpacing(-15 * Constants.SCALE);
            handGroup.getChildren().add(hBox);
            for (int i = 0; i < Constants.MAXIMUM_CARD_HAND_SIZE; i++) {
				loadedCards[i] = null;
				unitSlots[i] = new ImageView();
	            unitSlots[i].setFitWidth(Constants.SCREEN_WIDTH * 0.085);
    	        unitSlots[i].setFitHeight(Constants.SCREEN_WIDTH * 0.085);
                cards[i] = new Pane();
	            cards[i].getChildren().add(unitSlots[i]);
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

        addFinishButton();
    }

    private void updateNext() {
        next.getChildren().clear();
        final ImageView replaceIcon = new ImageView();
        next.getChildren().add(replaceIcon);
        replaceIcon.setFitWidth(Constants.SCREEN_WIDTH * 0.11);
        replaceIcon.setFitHeight(Constants.SCREEN_WIDTH * 0.11);
        replaceIcon.setImage(nextBack);
        Effect nullOrGrayscale = GameController.getInstance().getAvailableActions().canReplace(player) ? null : DISABLE_BUTTON_EFFECT;
        replaceIcon.setEffect(nullOrGrayscale);

        next.setOnMouseClicked(mouseEvent -> replaceSelectedCard());
    }

    private void updateCards() {
        for (int i = 0; i < Constants.MAXIMUM_CARD_HAND_SIZE; i++) {
			if (i >= player.getHand().size() && loadedCards[i] != null) {
				this.loadedCards[i].unloadView();
				this.loadedCards[i] = null;
			} else {
				if (i < player.getHand().size() && loadedCards[i] == null) {
					this.loadedCards[i] = createNewAnimationCard(this.cards[i], i, unitSlots[i]);
				} else if (loadedCards[i] != null && !loadedCards[i].getCardName().equals(player.getHand().get(i).getName())) {
					this.loadedCards[i].unloadView();
					this.loadedCards[i] = createNewAnimationCard(this.cards[i], i, unitSlots[i]);
				}
			}

			if (i < player.getHand().size())
				updateGreyScale(this.cards[i], i);

            if (this.selectedCard == i && this.loadedCards[i] != null) {
                unitSlots[i].setImage(this.cardBackGlow);
                loadedCards[i].inActive();
            } else {
                unitSlots[i].setImage(this.cardBack);
			}
        }
    }

	private CardAnimation createNewAnimationCard(Pane pane, int entry, ImageView imageView) {
        Card card = this.player.getHand().get(entry);
        CardAnimation cardAnimation = new CardAnimation(pane, this.player.getHand().get(entry),
            imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);

        if (cardAnimation != null) {
            pane.setOnMouseEntered(mouseEvent -> {
                if (this.cardPane != null) {
                    handGroup.getChildren().remove(this.cardPane);
                    this.cardPane = null;
                }
                if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                    SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.in_game_hove);
                    cardAnimation.inActive();
                    imageView.setImage(this.cardBackGlow);
                }
                this.cardPane = new CardPane(card, false, false, null);
                this.cardPane.setLayoutY(-300 * Constants.SCALE + pane.getLayoutY());
                this.cardPane.setLayoutX(150 * Constants.SCALE + pane.getLayoutX());
                handGroup.getChildren().add(this.cardPane);
            });

            pane.setOnMouseExited(mouseEvent -> {
                    if (this.cardPane != null) {
                        handGroup.getChildren().remove(this.cardPane);
                        this.cardPane = null;
                    }
                    if (this.selectedCard == entry) {
                        imageView.setImage(this.cardBackGlow);
                    } else {
                        imageView.setImage(this.cardBack);
                        cardAnimation.pause();
                    }
            });

            pane.setOnMouseClicked(mouseEvent -> {
                    if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                        clickOnCard(entry);
                    }
            });
			updateGreyScale(pane, entry);
			// Effect nullOrGrayscale = null;
            // boolean haveSufficientManaForCard = GameController.getInstance().getAvailableActions().haveSufficientMana(this.player, card);
			// // A card can be played if we have enough mana and if it is insertable
			// boolean canPlayCard = haveSufficientManaForCard && GameController.getInstance().getAvailableActions().canInsertCard(card);

			// if (!battleScene.isMyTurn() || !canPlayCard) {
			// 	nullOrGrayscale = DISABLE_BUTTON_EFFECT;
			// }

            // // Grey out cards in hand when its the opponents turn
            // // Effect nullOrGrayscale = battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card) && haveSufficientManaForCard ? null : DISABLE_BUTTON_EFFECT;
            // pane.setEffect(nullOrGrayscale);
	    }
		return (cardAnimation);
	}

	private void updateGreyScale(Pane pane, int entry) {
		Effect nullOrGrayscale = null;
        Card card = this.player.getHand().get(entry);
		AvailableActions actions = GameController.getInstance().getAvailableActions();
		boolean canPlayCard = actions.haveSufficientMana(this.player, card) && actions.canInsertCard(card);

		if (!battleScene.isMyTurn() || !canPlayCard)
			nullOrGrayscale = DISABLE_BUTTON_EFFECT;
        pane.setEffect(nullOrGrayscale);
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
                imageButton = new ImageButton(
                        "EXIT", event -> {
                    battleScene.getController().exitGameShow(new OnlineGame(battleScene.getGame()));
                    new MainMenu().show();
                },
                        new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_left@2x.png")),
                        new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_left_glow@2x.png"))
                );
            } else {
                imageButton = new ImageButton(
                        "FINISH", event -> battleScene.getController().forceFinish(),
                        new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_left@2x.png")),
                        new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_left_glow@2x.png"))
                );
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
            ImageButton imageButton = new ImageButton(
                    "GRAVEYARD", event -> showGraveyard(),
                    new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_right@2x.png")),
                    new Image(new FileInputStream("Client/src/main/resources/ui/button_primary_right_glow@2x.png"))
            );
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
            CardPane cardPane = new CardPane(card, false, false, null);
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
        if (player == null) return;
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

                    });
                } else {
                    Platform.runLater(() -> {
                        updateNext();
                        endTurnButton.setEffect(null);
                        endTurnLabel.setText("END TURN");
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
            int currentTimesReplacedThisTurn = GameController.getInstance().getAvailableActions().getNumTimesReplacedThisTurn();
            GameController.getInstance().getAvailableActions().setNumTimesReplacedThisTurn( currentTimesReplacedThisTurn + 1 );
            clickOnCard(selectedCard);
            updateNext();
        }
    }
}
