package view;

import controller.GameResultController;
import controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.gui.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static controller.SoundEffectPlayer.SoundName.victory_reward;
import static models.gui.UIConstants.*;

public class GameResultMenu extends Show {
    private static final double ICON_SIZE = 100 * SCALE;
    private static final String backgroundUrl = "Client/resources/backGrounds/battlemap6_middleground@2x.png";
    private static final Background ROOT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(40, 43, 53), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final Map<Boolean, ResultData> results = new HashMap<>();
    private static final String directory = "Client/resources/result_menu/";
    private static final String format = ".png";
    private static final double HERO_WIDTH = 3800 * SCALE;
    private static final double HERO_HEIGHT = 2875 * SCALE;
    private static final double MIDDLEGROUND_WIDTH = SCENE_WIDTH * 0.8;
    private static final double MIDDLEGROUND_HEIGHT = SCENE_HEIGHT * 0.5;
    private static final Font RESULT_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 100 * SCALE);
    private static final Font MIDDLE_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 70 * SCALE);

    static {
        results.put(true, new ResultData(
                "scene_diamonds_background_victory@2x",
                "scene_diamonds_middleground_victory@2x",
                "scene_diamonds_foreground_friendly@2x",
                "general_winner", ProfileGrid.goldIcon, "VICTORY",
                Color.rgb(115, 210, 255)
        ));
        results.put(false, new ResultData(
                "scene_diamonds_background_defeat@2x",
                "scene_diamonds_middleground_defeat@2x",
                "scene_diamonds_foreground_enemy@2x",
                "general_loser", ProfileGrid.generalIcon, "FAILURE",
                Color.rgb(255, 100, 115)
        ));
    }

    public GameResultMenu() {
        ResultData data = results.get(GameResultController.getInstance().amInWinner());
        try {
            root.setBackground(ROOT_BACKGROUND);
            BorderPane background = BackgroundMaker.getPlayBackground(backgroundUrl);

            ImageView resultBackground = ImageLoader.makeImageView(data.background, SCENE_WIDTH, SCENE_HEIGHT);
            ImageView resultMiddleground = ImageLoader.makeImageView(data.middleground, MIDDLEGROUND_WIDTH, MIDDLEGROUND_HEIGHT);
            resultMiddleground.relocate(SCENE_WIDTH * 0.1, SCENE_HEIGHT * 0.25);

            ImageView heroView = ImageLoader.makeImageView(data.hero, HERO_WIDTH, HERO_HEIGHT);
            heroView.relocate((SCENE_WIDTH - HERO_WIDTH) * 0.5, (SCENE_HEIGHT - HERO_HEIGHT) * 0.25);

            VBox resultBox = new VBox(DEFAULT_SPACING * 3);
            resultBox.setAlignment(Pos.CENTER);

            if (GameResultController.getInstance().amInWinner()) {
                data.middleText = GameResultController.getInstance().getReward() + "$";
            } else {
                data.middleText = "TRY AGAIN";
            }

            DefaultLabel resultLabel = new DefaultLabel(data.message, RESULT_FONT, Color.WHITE);
            resultLabel.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, data.color, 100 * SCALE, 0.7, 0, 0));

            HBox rewardBox = new HBox(DEFAULT_SPACING * 2);

            rewardBox.setAlignment(Pos.CENTER);
            ImageView icon = ImageLoader.makeImageView(data.middleIcon, ICON_SIZE, ICON_SIZE);
            DefaultLabel reward = new DefaultLabel(data.middleText, MIDDLE_FONT, Color.WHITE);

            rewardBox.getChildren().addAll(icon, reward);

            ImageButton playMenuButton = new ImageButton("PLAY MENU", event -> PlayMenu.getInstance().show());
            resultBox.getChildren().addAll(resultLabel, rewardBox, playMenuButton);


            VBox resultContainer = new VBox(resultBox);
            resultContainer.setAlignment(Pos.BOTTOM_CENTER);
            resultContainer.setMinSize(SCENE_WIDTH, SCENE_HEIGHT * 0.9);

            RadialVignette vignette = new RadialVignette(SCENE_WIDTH, SCENE_HEIGHT);

            ImageView resultForeground = ImageLoader.makeImageView(
                    results.get(GameResultController.getInstance().amInWinner()).foreground,
                    SCENE_WIDTH, SCENE_HEIGHT
            );

            AnchorPane sceneContents = new AnchorPane(
                    background, resultBackground, resultMiddleground,
                    heroView, vignette, resultContainer, resultForeground
            );


            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        super.show();
        if (GameResultController.getInstance().amInWinner()) {
            SoundEffectPlayer.getInstance().playSound(victory_reward);
        }
    }

    @Override
    public void showError(String message, EventHandler<? super MouseEvent> event) {

    }

    @Override
    public void showError(String message, String buttonText) {
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void showError(String message, String buttonText, EventHandler<? super MouseEvent> event) {
    }

    private static class ResultData {
        public final Color color;
        private final Image background;
        private final Image middleground;
        private final Image foreground;
        private final Image hero;
        private final Image middleIcon;
        private final String message;
        private String middleText;

        private ResultData(String backName, String middleName, String foreName, String heroName, Image middleIcon, String message, Color color) {
            background = ImageLoader.load(getUrl(backName));
            middleground = ImageLoader.load(getUrl(middleName));
            foreground = ImageLoader.load(getUrl(foreName));
            hero = ImageLoader.load(getUrl(heroName));
            this.middleIcon = middleIcon;
            this.message = message;
            this.color = color;
        }

        private String getUrl(String name) {
            return directory + name + format;
        }
    }
}
