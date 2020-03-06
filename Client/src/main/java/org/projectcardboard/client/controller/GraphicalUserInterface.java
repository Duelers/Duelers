package org.projectcardboard.client.controller;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.projectcardboard.client.models.gui.UIConstants;
import org.projectcardboard.client.models.gui.KeyboardShortcutConstants;
import org.projectcardboard.client.view.LoginMenu;

public class GraphicalUserInterface {
    private static GraphicalUserInterface GUI;
    private Stage stage;
    private Scene scene;
    private MediaPlayer backgroundMusicPlayer;
    private Media currentMedia;

    private GraphicalUserInterface() {
    }

    public static GraphicalUserInterface getInstance() {
        if (GUI == null) {
            GUI = new GraphicalUserInterface();
        }
        return GUI;
    }

    public void start(Stage stage) {
        this.stage = stage;
        new LoginMenu().show();
        setStageProperties(stage);
    }

    public void changeScene(AnchorPane root) {
        if (scene == null) {
            makeScene(root);
        } else {
            scene.setRoot(root);
        }
    }

    public void setBackgroundMusic(Media media) {
        try {
            if (media.equals(currentMedia)) return;
            currentMedia = media;
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.stop();
            }
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setVolume(0.05);
            backgroundMusicPlayer.setCycleCount(-1);
            backgroundMusicPlayer.setAutoPlay(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStageProperties(Stage stage) {
        stage.setTitle("CardBoard");

        stage.setFullScreen(true);
        stage.setResizable(true); // note that resizing the window does not currently 'redraw' the window.

        stage.setFullScreenExitHint(KeyboardShortcutConstants.EXIT_FULLSCREEN_HELP_MSG);
        stage.setFullScreenExitKeyCombination(KeyboardShortcutConstants.EXIT_FULLSCREEN);

        stage.show();
        stage.setOnCloseRequest(event -> Client.getInstance().close());
    }

    private void makeScene(AnchorPane root) {
        scene = new Scene(root, UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        //scene.getStylesheets().add(this.getClass().getResource("Client/resources/styles/scroll_pane.css").toExternalForm());
        scene.setCursor(UIConstants.DEFAULT_CURSOR);
        stage.setScene(scene);
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            currentMedia = null;
        }
    }

    public void closeFullscreen() {
        stage.setFullScreen(false);
    }

    public void makeFullScreen() {
        stage.setFullScreen(true);
    }
}
