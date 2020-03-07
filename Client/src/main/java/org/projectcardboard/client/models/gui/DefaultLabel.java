package org.projectcardboard.client.models.gui;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DefaultLabel extends Label {
    public DefaultLabel(String title, Font font, Color color) {
        this(title, font, color, 0, 0);
    }

    public DefaultLabel(String title, Font font, Color color, double x, double y) {
        super(title);
        setFont(font);
        setTextFill(color);
        relocate(x, y);
    }
}
