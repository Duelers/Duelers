package models.gui;

import javafx.scene.layout.HBox;

import java.io.FileNotFoundException;

class LoginBox extends HBox {

    LoginBox() throws FileNotFoundException {
        AbabeelLogoBox logoBox = new AbabeelLogoBox();
        LoginFieldsContainer loginBox = new LoginFieldsContainer();

        getChildren().addAll(logoBox, loginBox);
    }
}
