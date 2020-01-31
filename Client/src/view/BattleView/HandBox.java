package view.BattleView;

import controller.GameController;
import controller.SoundEffectPlayer;
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
import models.comperessedData.CompressedCard;
import models.comperessedData.CompressedPlayer;
import models.gui.*;
import models.message.OnlineGame;
import view.MainMenu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class HandBox implements PropertyChangeListener {
    private static final Effect DISABLE_BUTTON_EFFECT = new ColorAdjust(0, -1, 0, 0);
    private final BattleScene battleScene;
    private final CompressedPlayer player;
    private final Group handGroup;
    private final Pane[] cards = new Pane[5];
    private final Pane next = new Pane();
    private final Pane[] items = new Pane[3];
    private int selectedCard = -1;
    private int selectedItem = -1;
    private CardPane cardPane = null;
    private Image cardBack = new Image(new FileInputStream("Client/resources/ui/card_background@2x.png"));
    private Image cardBackGlow = new Image(new FileInputStream("Client/resources/ui/card_background_highlight@2x.png"));
    private Image nextBack = new Image(new FileInputStream("Client/resources/ui/replace_background@2x.png"));
    private Image nextRingSmoke = new Image(new FileInputStream("Client/resources/ui/replace_outer_ring_smoke@2x.png"));
    private Image nextRingShine = new Image(new FileInputStream("Client/resources/ui/replace_outer_ring_shine@2x.png"));
    private Image endTurnImage = new Image(new FileInputStream("Client/resources/ui/button_end_turn_finished@2x.png"));
    private Image endTurnImageGlow = new Image(new FileInputStream("Client/resources/ui/button_end_turn_finished_glow@2x.png"));
    private DefaultLabel endTurnLabel;
    private StackPane endTurnButton;


    HandBox(BattleScene battleScene, CompressedPlayer player, double x, double y) throws Exception {
        this.battleScene = battleScene;
        this.player = player;
        handGroup = new Group();
        handGroup.setLayoutX(x);
        handGroup.setLayoutY(y);


        if (player != null) {
            HBox hBox = new HBox();
            hBox.setLayoutX(200 * Constants.SCALE);
            hBox.setLayoutY(25 * Constants.SCALE);
            hBox.setSpacing(-15 * Constants.SCALE);
            handGroup.getChildren().add(hBox);
            for (int i = 0; i < 5; i++) {
                cards[i] = new Pane();
                hBox.getChildren().add(cards[i]);
            }
            for (int i = 0; i < 3; i++) {
                items[i] = new Pane();
                hBox.getChildren().add(items[i]);
            }
            updateCards();
            updateItems();

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
        final ImageView imageView1 = new ImageView();
        next.getChildren().add(imageView1);
        imageView1.setFitWidth(Constants.SCREEN_WIDTH * 0.11);
        imageView1.setFitHeight(Constants.SCREEN_WIDTH * 0.11);
        imageView1.setImage(nextBack);

        final ImageView imageView2 = new ImageView();
        next.getChildren().add(imageView2);
        imageView2.setFitWidth(Constants.SCREEN_WIDTH * 0.11);
        imageView2.setFitHeight(Constants.SCREEN_WIDTH * 0.11);

        final CardAnimation cardAnimation;
        if (player.getNextCard() != null) {
            cardAnimation = new CardAnimation(next, player.getNextCard(),
                    imageView1.getLayoutY() + imageView1.getFitHeight() / 2, imageView1.getLayoutX() + imageView1.getFitWidth() / 2);
        } else {
            cardAnimation = null;
        }

        imageView2.setImage(nextRingSmoke);

        if (cardAnimation != null) {
            next.setOnMouseEntered(mouseEvent -> {
                if (battleScene.isMyTurn()) {
                    cardAnimation.inActive();
                    imageView2.setImage(nextRingShine);
                }
            });
            next.setOnMouseExited(mouseEvent -> {
                cardAnimation.pause();
                imageView2.setImage(nextRingSmoke);
            });
        }
    }

    private void updateCards() {
        for (int i = 0; i < 5; i++) {
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
                final CompressedCard card = player.getHand().get(I);
                cards[i].setOnMouseEntered(mouseEvent -> {
                    if (cardPane != null) {
                        handGroup.getChildren().remove(cardPane);
                        cardPane = null;
                    }
                    if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.in_game_hove);
                        cardAnimation.inActive();
                        imageView.setImage(cardBackGlow);
                    }
                    try {
                        cardPane = new CardPane(card, false, false, null);
                        cardPane.setLayoutY(-300 * Constants.SCALE + cards[I].getLayoutY());
                        cardPane.setLayoutX(150 * Constants.SCALE + cards[I].getLayoutX());
                        handGroup.getChildren().add(cardPane);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
                    if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                        clickOnCard(I);
                    }
                });
            }
        }
    }

    private void updateItems() {
        try {
            for (int i = 0; i < 3; i++) {
                final int I = i;
                items[i].getChildren().clear();
                final ImageView imageView = new ImageView();
                items[i].getChildren().add(imageView);
                imageView.setFitWidth(Constants.SCREEN_WIDTH * 0.065);
                imageView.setFitHeight(Constants.SCREEN_WIDTH * 0.065);
                imageView.setLayoutY(Constants.SCREEN_WIDTH * 0.01);

                final CardAnimation cardAnimation;
                if (player.getCollectedItems().size() > i) {
                    cardAnimation = new CardAnimation(items[i], player.getCollectedItems().get(i),
                            imageView.getFitHeight() / 2 + Constants.SCREEN_WIDTH * 0.01, imageView.getFitWidth() / 2);
                } else {
                    cardAnimation = null;
                }

                if (selectedItem == i && cardAnimation != null) {
                    imageView.setImage(cardBackGlow);
                    cardAnimation.inActive();
                } else
                    imageView.setImage(cardBack);

                if (cardAnimation != null) {
                    final CompressedCard card = player.getCollectedItems().get(I);
                    items[i].setOnMouseEntered(mouseEvent -> {
                        if (cardPane != null) {
                            handGroup.getChildren().remove(cardPane);
                            cardPane = null;
                        }
                        if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.in_game_hove);
                            cardAnimation.inActive();
                            imageView.setImage(cardBackGlow);
                        } else {
                            imageView.setImage(cardBack);
                        }
                        try {
                            cardPane = new CardPane(card, false, false, null);
                            cardPane.setLayoutY(-300 * Constants.SCALE + items[I].getLayoutY());
                            cardPane.setLayoutX(75 * Constants.SCALE + items[I].getLayoutX());
                            handGroup.getChildren().add(cardPane);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });

                    items[i].setOnMouseExited(mouseEvent -> {
                        if (cardPane != null) {
                            handGroup.getChildren().remove(cardPane);
                            cardPane = null;
                        }
                        if (selectedItem == I) {
                            imageView.setImage(cardBackGlow);
                        } else {
                            imageView.setImage(cardBack);
                            cardAnimation.pause();
                        }
                    });

                    items[i].setOnMouseClicked(mouseEvent -> {
                        if (battleScene.isMyTurn() && GameController.getInstance().getAvailableActions().canInsertCard(card)) {
                            clickOnItem(I);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error making hand items");
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
                imageButton = new ImageButton(
                        "EXIT", event -> {
                            battleScene.getController().exitGameShow(new OnlineGame(battleScene.getGame()));
                            new MainMenu().show();
                },
                        new Image(new FileInputStream("Client/resources/ui/button_primary_left@2x.png")),
                        new Image(new FileInputStream("Client/resources/ui/button_primary_left_glow@2x.png"))
                );
            } else {
                imageButton = new ImageButton(
                        "FINISH", event -> battleScene.getController().forceFinish(),
                        new Image(new FileInputStream("Client/resources/ui/button_primary_left@2x.png")),
                        new Image(new FileInputStream("Client/resources/ui/button_primary_left_glow@2x.png"))
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
                    new Image(new FileInputStream("Client/resources/ui/button_primary_right@2x.png")),
                    new Image(new FileInputStream("Client/resources/ui/button_primary_right_glow@2x.png"))
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

        List<CompressedCard> graveyard = player.getGraveyard();
        for (int i = 0; i < graveyard.size(); i++) {
            CompressedCard card = graveyard.get(i);
            try {
                CardPane cardPane = new CardPane(card, false, false, null);
                cardsPane.add(cardPane, i % 3, i / 3);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            case "items":
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
            selectedItem = -1;
            updateItems();
            battleScene.getMapBox().resetSelection();
        }
        updateCards();
    }

    private void clickOnItem(int i) {
        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
        if (selectedItem == i) {
            selectedItem = -1;
            battleScene.getMapBox().updateMapColors();
        } else {
            selectedItem = i;
            selectedCard = -1;
            updateCards();
            battleScene.getMapBox().resetSelection();
        }
        updateItems();
    }

    Group getHandGroup() {
        return handGroup;
    }

    CompressedCard getSelectedCard() {
        if (selectedCard >= 0)
            return player.getHand().get(selectedCard);
        if (selectedItem >= 0)
            return player.getCollectedItems().get(selectedItem);
        return null;
    }

    void resetSelection() {
        if (player != null) {
            selectedCard = -1;
            selectedItem = -1;
            updateCards();
            updateItems();
            updateNext();
        }
    }
}
