package org.projectcardboard.client.models.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import java.io.FileNotFoundException;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class LoginMenuLogoBox extends VBox {
  private static final String ABABEEL_URL = "/ui/meme_logo.png";
  private static final Paint ABABEEL_TEXT_COLOR = Color.rgb(102, 166, 214);
  private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 35 * SCALE);
  private static final Background ABABEEL_BACKGROUND =
      new Background(new BackgroundFill(Color.rgb(15, 16, 11), CornerRadii.EMPTY, Insets.EMPTY));
  private static final double LOGO_WIDTH = 700 * SCALE;
  private static final double LOGO_HEIGHT = 425 * SCALE;
  private static final double BOX_SIZE = 800 * SCALE;

  LoginMenuLogoBox() throws FileNotFoundException {
    super(UIConstants.DEFAULT_SPACING * 10);
    ImageView logoView = ImageLoader.loadImage(ABABEEL_URL, LOGO_WIDTH, LOGO_HEIGHT);
    Text text = WelcomeMessage();

    getChildren().addAll(logoView, text);
    setAlignment(Pos.CENTER);
    setBackground(ABABEEL_BACKGROUND);
    setMinSize(BOX_SIZE, BOX_SIZE);
    setMaxSize(BOX_SIZE, BOX_SIZE);
  }

  private Text WelcomeMessage() {
    return new DefaultText(
        LanguageData.getInstance()
            .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.WELCOME_MESSAGE}),
        BOX_SIZE - UIConstants.DEFAULT_SPACING * 3, FONT, ABABEEL_TEXT_COLOR);
  }
}
