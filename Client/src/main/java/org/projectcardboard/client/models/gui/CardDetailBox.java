package org.projectcardboard.client.models.gui;

import static org.projectcardboard.client.models.gui.CardBackground.GLOW_WIDTH;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import shared.UtilityFunctions;
import shared.models.card.CardType;
import shared.models.card.ICard;

class CardDetailBox extends VBox {
  static final Font DESCRIPTION_FONT = Font.font("SansSerif", FontWeight.BOLD, 19 * SCALE);
  static final Color DESCRIPTION_COLOR = Color.rgb(133, 199, 202, 0.7);
  private static final Insets BOX_PADDING = new Insets(255 * SCALE, 0, 0, 0);
  private static final Font NAME_FONT =
      Font.font("DejaVu Sans Light", FontWeight.SEMI_BOLD, 28 * SCALE);
  private static final Font TYPE_FONT =
      Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 25 * SCALE);
  private static final Color NAME_COLOR = Color.gray(1);
  private static final Color TYPE_COLOR = Color.rgb(133, 199, 202);
  private static final double DESCRIPTION_WIDTH = 400 * SCALE;
  private static final double SPACE_HEIGHT = 115 * SCALE;

  CardDetailBox(ICard card) {
    super(UIConstants.DEFAULT_SPACING);
    setPadding(BOX_PADDING);
    setMinWidth(GLOW_WIDTH);
    setAlignment(Pos.CENTER);

    DefaultLabel name = new DefaultLabel(card.getName(), NAME_FONT, NAME_COLOR);

    String facName = UtilityFunctions.capitaliseString(card.getFaction());
    DefaultLabel faction = new DefaultLabel(facName, TYPE_FONT, TYPE_COLOR);

    DefaultText description = new DefaultText(card.getDescription(), DESCRIPTION_WIDTH,
        DESCRIPTION_FONT, DESCRIPTION_COLOR);

    getChildren().addAll(name, faction, new Space(SPACE_HEIGHT), description);
  }
}
