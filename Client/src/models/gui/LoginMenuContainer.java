package models.gui;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

public class LoginMenuContainer extends VBox {
    private static final String DUELYST_LOGO_URL = "Client/resources/ui/brand_duelyst.png";
    private static final double WIDTH = 1600 * SCALE;

    public LoginMenuContainer() throws FileNotFoundException {
        super(UIConstants.DEFAULT_SPACING);
        setMaxWidth(WIDTH);
        setAlignment(Pos.CENTER);

        ImageView brandView = ImageLoader.loadImage(
                DUELYST_LOGO_URL, UIConstants.DUELYST_LOGO_WIDTH, UIConstants.DUELYST_LOGO_HEIGHT
        );
        LoginBox horizontalBox = new LoginBox();

        getChildren().addAll(brandView, horizontalBox);
    }
}
