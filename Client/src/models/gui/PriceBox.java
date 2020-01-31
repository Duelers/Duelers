package models.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import static models.gui.CardBackground.GLOW_WIDTH;
import static models.gui.CardDetailBox.DESCRIPTION_COLOR;
import static models.gui.CardDetailBox.DESCRIPTION_FONT;
import static models.gui.UIConstants.SCALE;

class PriceBox extends VBox {
    private static final double PRICE_Y = 430 * SCALE;
    private final DefaultLabel priceLabel;

    PriceBox(int price) {
        priceLabel = new DefaultLabel(
                "PRICE: " + price, DESCRIPTION_FONT, DESCRIPTION_COLOR
        );
        setMinWidth(GLOW_WIDTH);
        setAlignment(Pos.CENTER);
        setLayoutY(PRICE_Y);
        getChildren().add(priceLabel);
    }

    void setPrice(int newValue) {
        priceLabel.setText("PRICE: " + newValue);
    }
}
