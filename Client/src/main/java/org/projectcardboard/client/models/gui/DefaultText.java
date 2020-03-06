package org.projectcardboard.client.models.gui;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DefaultText extends Text {

    public DefaultText(String text, double wrapWidth, Font font, Paint color) {
        super(text);
        setTextAlignment(TextAlignment.CENTER);
        setWrappingWidth(wrapWidth);
        setFont(font);
        setFill(color);
    }
}
