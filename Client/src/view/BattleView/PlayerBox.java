package view.BattleView;

import controller.GameController;
import controller.SoundEffectPlayer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.comperessedData.CompressedGame;
import models.comperessedData.CompressedPlayer;
import models.gui.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static java.lang.Math.pow;
import static view.BattleView.Constants.SCALE;
import static view.BattleView.Constants.SCREEN_WIDTH;

public class PlayerBox implements PropertyChangeListener {
    private final Image manaImage = new Image(new FileInputStream("Client/resources/ui/icon_mana@2x.png"));
    private final Image inActiveManaImage = new Image(new FileInputStream("Client/resources/ui/icon_mana_inactive@2x.png"));
    private final Image chatImage = new Image(new FileInputStream("Client/resources/ui/chat_bubble.png"));
    private final double CHAT_BUBBLE_SIZE = 150 * SCALE;
    private final NormalField chatField = new NormalField("Type message and send");
    private final BattleScene battleScene;
    private final CompressedPlayer player1, player2;
    private final Group group;
    private final Group mpGroup;
    private MessageShow player1MessageShow;
    private MessageShow player2MessageShow;
    private ImageView player1Image;
    private ImageView player2Image;
    private ColorAdjust player1ImageEffect;
    private ColorAdjust player2ImageEffect;

    PlayerBox(BattleScene battleScene, CompressedGame game) throws Exception {
        this.battleScene = battleScene;
        this.player1 = game.getPlayerOne();
        this.player2 = game.getPlayerTwo();

        group = new Group();
        addHeroPortraits();
        if (game.getTurnNumber() % 2 == 1) {
            player1ImageEffect.setBrightness(0);
            player2ImageEffect.setBrightness(-0.6);
        } else {
            player1ImageEffect.setBrightness(-0.6);
            player2ImageEffect.setBrightness(0);
        }
        mpGroup = new Group();
        group.getChildren().add(mpGroup);
        updateMP(3);

        if (battleScene.getMyPlayerNumber() != -1) {
            addChatField();
            makeMessageShows();
        }

        game.addPropertyChangeListener(this);
    }

    private HashMap<String, String> MapGeneralToPortrait() {
        // Note that this is a quick hack; a better solution is to have "portraitId" defined in the Hero's json file.
        HashMap<String, String> nameToPortrait = new HashMap<>();
        nameToPortrait.put("Reva Eventide", "Client/resources/photo/general_portrait_image_hex_f2-alt@2x.png");
        nameToPortrait.put("Vaath The Immortal", "Client/resources/photo/general_portrait_image_hex_f5@2x.png");
        nameToPortrait.put("Argeon Highmayne", "Client/resources/photo/general_portrait_image_hex_f1@2x.png");
        nameToPortrait.put("Faie Bloodwing", "Client/resources/photo/general_portrait_image_hex_f6@2x.png");

        return nameToPortrait;
    }

    private void addChatField() {
        double x, y;
        chatField.setMinWidth(CHAT_BUBBLE_SIZE);
        chatField.setMaxWidth(CHAT_BUBBLE_SIZE);
        if (battleScene.getMyPlayerNumber() == 2) {
            x = player1Image.getX() + (player1Image.getFitWidth() - chatField.getMinWidth()) / 2;
            y = player1Image.getY() + player1Image.getFitHeight();
        } else {
            x = player2Image.getX() + (player2Image.getFitWidth() - chatField.getMinWidth()) / 2;
            y = player2Image.getY() + player2Image.getFitHeight();
        }
        chatField.relocate(x, y);
        chatField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GameController.getInstance().sendChat(chatField.getText());
                battleScene.showMyMessage(chatField.getText());
                chatField.clear();
            }
        });

        group.getChildren().add(chatField);
    }

    private void makeMessageShows() {
        player1MessageShow = new MessageShow(player1Image);
        player2MessageShow = new MessageShow(player2Image);
    }

    private void addHeroPortraits() throws FileNotFoundException {

        String player1HeroName = player1.getHero().getCard().getName();
        Image player1Profile = new Image(new FileInputStream(MapGeneralToPortrait().getOrDefault(player1HeroName, "Client/resources/photo/general_portrait_image_hex_rook@2x.png")));

        String player2HeroName = player2.getHero().getCard().getName();
        Image player2Profile = new Image(new FileInputStream(MapGeneralToPortrait().getOrDefault(player2HeroName, "Client/resources/photo/general_portrait_image_hex_calibero@2x.png")));

        player1Image = ImageLoader.makeImageView(player1Profile,
                player1Profile.getWidth() * SCALE * 0.3,
                player1Profile.getHeight() * SCALE * 0.3
        );
        player2Image = ImageLoader.makeImageView(player2Profile,
                player2Profile.getWidth() * SCALE * 0.3,
                player2Profile.getHeight() * SCALE * 0.3
        );
        player1Image.setX(Constants.SCREEN_WIDTH * 0.01);
        player1Image.setY(-Constants.SCREEN_HEIGHT * 0.02);
        player2Image.setX(Constants.SCREEN_WIDTH * 0.85);
        player2Image.setY(-Constants.SCREEN_HEIGHT * 0.02);
        DefaultLabel player1Name = new DefaultLabel("", Constants.NAME_FONT, Color.WHITE, 290 * SCALE, 75 * SCALE);
        player1Name.setBackground(new Background(new BackgroundFill(Color.rgb(155, 82, 100, 0.7), new CornerRadii(3), Insets.EMPTY)));
        player1Name.setPadding(Constants.NAME_PADDING);
        DefaultLabel player2Name = new DefaultLabel("", Constants.NAME_FONT, Color.WHITE, SCREEN_WIDTH - 600 * SCALE, 75 * SCALE);
        player2Name.setBackground(new Background(new BackgroundFill(Color.rgb(155, 82, 100, 0.7), new CornerRadii(3), Insets.EMPTY)));
        player2Name.setPadding(Constants.NAME_PADDING);
        player1Name.setText(player1.getUserName());
        player2Name.setText(player2.getUserName());
        player1ImageEffect = new ColorAdjust();
        player2ImageEffect = new ColorAdjust();
        player1Image.setEffect(player1ImageEffect);
        player2Image.setEffect(player2ImageEffect);
        group.getChildren().addAll(player1Image, player2Image, player1Name, player2Name);
    }

    private void updateMP(int maxMP) {
        mpGroup.getChildren().clear();
        for (int i = 1; i <= player1.getCurrentMP(); i++) {
            try {
                ImageView imageView = new ImageView(manaImage);
                imageView.setFitWidth(imageView.getImage().getWidth() * SCALE * 0.35);
                imageView.setFitHeight(imageView.getImage().getHeight() * SCALE * 0.35);
                imageView.setX(SCALE * (250 + i * 40));
                imageView.setY(SCALE * (150 - i * 2));
                mpGroup.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = player1.getCurrentMP() + 1; i <= maxMP; i++) {
            try {
                ImageView imageView = new ImageView(inActiveManaImage);
                imageView.setFitWidth(imageView.getImage().getWidth() * SCALE * 0.35);
                imageView.setFitHeight(imageView.getImage().getHeight() * SCALE * 0.35);
                imageView.setX(SCALE * (250 + i * 40));
                imageView.setY(SCALE * (150 - i * 2));
                mpGroup.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= player2.getCurrentMP(); i++) {
            try {
                ImageView imageView = new ImageView(manaImage);
                imageView.setFitWidth(imageView.getImage().getWidth() * SCALE * 0.35);
                imageView.setFitHeight(imageView.getImage().getHeight() * SCALE * 0.35);
                imageView.setX(Constants.SCREEN_WIDTH - SCALE * (250 + i * 40) - imageView.getFitWidth());
                imageView.setY(SCALE * (150 - i * 2));
                mpGroup.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = player2.getCurrentMP() + 1; i <= maxMP; i++) {
            try {
                ImageView imageView = new ImageView(inActiveManaImage);
                imageView.setFitWidth(imageView.getImage().getWidth() * SCALE * 0.35);
                imageView.setFitHeight(imageView.getImage().getHeight() * SCALE * 0.35);
                imageView.setX(Constants.SCREEN_WIDTH - SCALE * (250 + i * 40) - imageView.getFitWidth());
                imageView.setY(SCALE * (150 - i * 2));
                mpGroup.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Group getGroup() {
        return group;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("mp1")) {
            Platform.runLater(() -> {
                updateMP((int) evt.getNewValue());
                battleScene.getHandBox().resetSelection();
                battleScene.getMapBox().resetSelection();
            });
        }
        if (evt.getPropertyName().equals("mp2")) {
            Platform.runLater(() -> {
                updateMP((int) evt.getNewValue());
                battleScene.getHandBox().resetSelection();
                battleScene.getMapBox().resetSelection();
            });
        }
        if (evt.getPropertyName().equals("turn")) {
            Platform.runLater(() -> {
                battleScene.getHandBox().resetSelection();
                battleScene.getMapBox().resetSelection();
                if ((int) evt.getNewValue() % 2 == 1) {
                    player1ImageEffect.setBrightness(0);
                    player2ImageEffect.setBrightness(-0.6);
                } else {
                    player1ImageEffect.setBrightness(-0.6);
                    player2ImageEffect.setBrightness(0);
                }
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.your_turn);
            });
        }
    }

    void showMessage(CompressedPlayer player, String text) {
        if (player.getPlayerNumber() == 1) {
            player1MessageShow.show(text);
        } else {
            player2MessageShow.show(text);
        }
    }

    class MessageShow extends AnimationTimer {
        private final long showTime = (long) (4 * pow(10, 9));
        private final DefaultText text = new DefaultText("", CHAT_BUBBLE_SIZE * 0.9, UIConstants.DEFAULT_FONT, Color.BLACK);
        private final StackPane stackPane;
        private long initialTime = -1;

        MessageShow(ImageView playerImage) {
            double x = playerImage.getX() + (playerImage.getFitWidth() - CHAT_BUBBLE_SIZE) / 2;
            double y = playerImage.getY() + playerImage.getFitHeight();
            ImageView chatView = ImageLoader.makeImageView(chatImage, CHAT_BUBBLE_SIZE, CHAT_BUBBLE_SIZE);
            stackPane = new StackPane(chatView, text);
            stackPane.relocate(x, y);
        }

        void show(String text) {
            this.text.setText(text);
            initialTime = -1;
            start();
        }

        @Override
        public void handle(long now) {
            if (initialTime == -1) {
                initialTime = now;
                group.getChildren().remove(stackPane);
                group.getChildren().add(stackPane);
            }
            if (now - initialTime > showTime) {
                group.getChildren().remove(stackPane);
                stop();
            }
        }
    }
}