package org.projectcardboard.client.models.gui;

import javafx.scene.layout.HBox;

import java.io.FileNotFoundException;

class LoginBox extends HBox {

  LoginBox() throws FileNotFoundException {
    LoginMenuLogoBox logoBox = new LoginMenuLogoBox();
    LoginFieldsContainer loginBox = new LoginFieldsContainer();

    getChildren().addAll(logoBox, loginBox);
  }
}
