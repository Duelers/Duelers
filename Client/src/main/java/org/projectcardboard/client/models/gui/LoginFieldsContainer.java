package org.projectcardboard.client.models.gui;

import javafx.scene.Cursor;
import org.projectcardboard.client.controller.LoginMenuController;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.select;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class LoginFieldsContainer extends VBox {
  private static final Background LOGIN_BOX_BACKGROUND = new Background(
      new BackgroundFill(Color.rgb(100, 100, 100, 0.6), CornerRadii.EMPTY, Insets.EMPTY));
  private static final double BOX_SIZE = 800 * SCALE;
  private static final Insets PADDING = new Insets(60 * SCALE, 20 * SCALE, 20 * SCALE, 20 * SCALE);
  private final NormalField usernameField;
  private final NormalPasswordField passwordField;

  LoginFieldsContainer() {
    super(UIConstants.DEFAULT_SPACING * 2);

    String username = LanguageData.getInstance()
        .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.USERNAME});
    usernameField = new NormalField(username);
    passwordField = new NormalPasswordField();

    Region space = new Region();
    space.setMinHeight(BOX_SIZE * 0.5);

    HBox buttons = makeButtonsBox();

    getChildren().addAll(usernameField, passwordField, space, buttons);
    setPadding(PADDING);
    setBackground(LOGIN_BOX_BACKGROUND);
    setMinSize(BOX_SIZE, BOX_SIZE);
    setMaxSize(BOX_SIZE, BOX_SIZE);
  }

  private HBox makeButtonsBox() {
    String login = LanguageData.getInstance()
        .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.LOGIN});
    OrangeButton loginButton = new OrangeButton(login, event -> LoginMenuController.getInstance()
        .login(usernameField.getText(), passwordField.getText()), select, true);

    String register = LanguageData.getInstance()
        .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.REGISTER});
    OrangeButton registerButton = new OrangeButton(register, event -> LoginMenuController
        .getInstance().register(usernameField.getText(), passwordField.getText()), select, true);

    setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.ENTER)) {
        LoginMenuController.getInstance().login(usernameField.getText(), passwordField.getText());
        setCursor(Cursor.WAIT);

      }

    });

    return new HBox(UIConstants.DEFAULT_SPACING, loginButton, registerButton);
  }
}
