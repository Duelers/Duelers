package models.gui;

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

import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

class AbabeelLogoBox extends VBox {
    private static final String ABABEEL_URL = "Client/resources/ui/logo.png";
    private static final Paint ABABEEL_TEXT_COLOR = Color.rgb(102, 166, 214);
    private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE);
    private static final Background ABABEEL_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(15, 16, 11), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final double LOGO_WIDTH = 700 * SCALE;
    private static final double LOGO_HEIGHT = 425 * SCALE;
    private static final double BOX_SIZE = 800 * SCALE;

    AbabeelLogoBox() throws FileNotFoundException {
        super(UIConstants.DEFAULT_SPACING * 10);
        ImageView logoView = ImageLoader.loadImage(ABABEEL_URL, LOGO_WIDTH, LOGO_HEIGHT);
        Text text = makeAbabeelText();

        getChildren().addAll(logoView, text);
        setAlignment(Pos.CENTER);
        setBackground(ABABEEL_BACKGROUND);
        setMinSize(BOX_SIZE, BOX_SIZE);
        setMaxSize(BOX_SIZE, BOX_SIZE);
    }

    private Text makeAbabeelText() {
        return new DefaultText(
                "LOGIN TO DUELYST USING YOUR ABABEEL ACCOUNT",
                BOX_SIZE - UIConstants.DEFAULT_SPACING * 5,
                FONT, ABABEEL_TEXT_COLOR
        );
    }
}
