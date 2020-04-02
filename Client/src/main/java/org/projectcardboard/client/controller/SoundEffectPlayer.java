package org.projectcardboard.client.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundEffectPlayer {
  private static final Map<SoundName, Media> mediaFiles = new HashMap<>();
  private static final String directory = "/sfx/";
  private static final String format = ".m4a";
  private static final SoundEffectPlayer SEP = new SoundEffectPlayer();

  Logger logger = LoggerFactory.getLogger(SoundEffectPlayer.class);

  static {
    Arrays.stream(SoundName.values()).forEach(soundName -> {
      URL soundEffectResource = SoundEffectPlayer.class.getResource(soundName.path);
      mediaFiles.put(soundName, new Media(soundEffectResource.toString()));
    });
  }

  private SoundEffectPlayer() {}

  public static SoundEffectPlayer getInstance() {
    return SEP;
  }

  public void playSound(SoundName soundName) {
    if (soundName == null) {
      logger.warn("Cannot play sound since soundName is null");
      return;
    }
    try {
      Media media = mediaFiles.get(soundName);
      MediaPlayer mediaPlayer = new MediaPlayer(media);
      mediaPlayer.setVolume(1);
      mediaPlayer.play();
    } catch (Exception e) {
      logger.warn("Failed to play sound");
      logger.trace(e.getMessage());
    }
  }

  public enum SoundName {
    error("sfx_ui_error"), hover("sfx_ui_menu_hover"), open_dialog(
        "sfx_ui_panel_swoosh_enter"), close_dialog("sfx_ui_panel_swoosh_exit"), select(
            "sfx_ui_select"), enter_page("sfx_ui_modalwindow_swoosh_enter"), exit_page(
                "sfx_ui_modalwindow_swoosh_exit"), your_turn("sfx_ui_yourturn"), click(
                    "sfx_unit_onclick"), in_game_hove("sfx_ui_in_game_hover"), attack(
                        "sfx_neutral_whitewidow_attack_swing"), hit(
                            "sfx_neutral_chaoselemental_hit"), death(
                                "sfx_neutral_gambitgirl_death"), victory_match(
                                    "sfx_victory_match_w_vo"), lose_match(
                                        "sfx_victory_crest"), victory_reward(
                                            "sfx_victory_reward_chant"), move(
                                                "sfx_unit_run_charge_4");

    private final String path;

    SoundName(String name) {
      this.path = directory + name + format;
    }
  }
}
