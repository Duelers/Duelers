package models.gui;

import javafx.geometry.Insets;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.languageLocalisation.LanguageData;
import models.languageLocalisation.LanguageKeys;

class NormalPasswordField extends PasswordField {
    private static final Background TEXT_FIELD_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );

    NormalPasswordField() {
        setFont(UIConstants.DEFAULT_FONT);
        setBackground(TEXT_FIELD_BACKGROUND);
        setBorder(DEFAULT_BORDER);
        setPadding(new Insets(UIConstants.DEFAULT_SPACING * 2));

        String password = LanguageData.getInstance().getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.PASSWORD});
        setPromptText(password);
        setStyle("-fx-text-inner-color: #fffbfd;");
    }
}
