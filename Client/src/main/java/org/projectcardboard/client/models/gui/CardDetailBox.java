package org.projectcardboard.client.models.gui;

import static org.projectcardboard.client.models.gui.CardBackground.GLOW_WIDTH;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import shared.models.card.CardType;
import shared.models.card.ICard;

class CardDetailBox extends VBox {
    static final Font DESCRIPTION_FONT = Font.font("SansSerif", FontWeight.BOLD, 19 * SCALE);
    static final Color DESCRIPTION_COLOR = Color.rgb(133, 199, 202, 0.7);
    private static final Insets BOX_PADDING = new Insets(255 * SCALE, 0, 0, 0);
    private static final Font NAME_FONT = Font.font("DejaVu Sans Light", FontWeight.SEMI_BOLD, 28 * SCALE);
    private static final Font TYPE_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 25 * SCALE);
    private static final Color NAME_COLOR = Color.gray(1);
    private static final Color TYPE_COLOR = Color.rgb(133, 199, 202);
    private static final double DESCRIPTION_WIDTH = 400 * SCALE;
    private static final double SPACE_HEIGHT = 115 * SCALE;
    private final DefaultLabel name;
    private final DefaultLabel type;
    private final DefaultText description;

    CardDetailBox(ICard card) {
        super(UIConstants.DEFAULT_SPACING);
        setPadding(BOX_PADDING);
        setMinWidth(GLOW_WIDTH);
        setAlignment(Pos.CENTER);

        name = new DefaultLabel(card.getName(), NAME_FONT, NAME_COLOR);
        type = new DefaultLabel(
                card.getType().toString().replace("_", " "), TYPE_FONT, TYPE_COLOR
        );
        description = new DefaultText(
                card.getDescription(), DESCRIPTION_WIDTH, DESCRIPTION_FONT, DESCRIPTION_COLOR
        );

        getChildren().addAll(name, type, new Space(SPACE_HEIGHT), description);
    }

    void setName(String newValue) {
        name.setText(newValue);
    }

    void setType(CardType newValue) {
        type.setText(newValue.toString().replace("_", " "));
    }

    void setDescription(String newValue) {
        description.setText(newValue);
    }
}
