package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class PlayButtonImageZone extends StackPane {
  private static final Font FONT = Font.font("SansSerif", FontWeight.BOLD, 25 * SCALE);
  private static final Effect HOVER_EFFECT = new ColorAdjust(0, 0.15, 0.05, 0);
  private static final Effect TEXT_SHADOW = new DropShadow(20 * SCALE, Color.WHITE);
  private static final double DESCRIPTION_WRAP =
      PlayButtonBox.BUTTON_WIDTH - UIConstants.DEFAULT_SPACING * 2;

  PlayButtonImageZone(PlayButtonItem item) {
    Space space = new Space(PlayButtonBox.BUTTON_HEIGHT * 0.75);
    Label title = new PlayButtonTitle(item.title);
    Separator separator = new DefaultSeparator(Orientation.HORIZONTAL);
    Text description = new DefaultText(item.description, DESCRIPTION_WRAP, FONT, Color.WHITE);

    VBox textBox = new VBox(UIConstants.DEFAULT_SPACING * 2, space, title, separator, description);
    textBox.setAlignment(Pos.CENTER);

    LinearVignette vignette =
        new LinearVignette(PlayButtonBox.BUTTON_WIDTH, PlayButtonBox.BUTTON_HEIGHT);

    getChildren().addAll(item.imageView, vignette, textBox);

    setOnMouseEntered(event -> {
      item.imageView.setEffect(HOVER_EFFECT);
      SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
      setCursor(UIConstants.SELECT_CURSOR);
      title.setEffect(TEXT_SHADOW);
    });

    setOnMouseExited(event -> {
      item.imageView.setEffect(null);
      setCursor(UIConstants.DEFAULT_CURSOR);
      title.setEffect(null);
    });

    setOnMouseClicked(event -> {
      SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
      item.event.handle(event);
    });
  }
}
