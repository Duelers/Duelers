package models.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.List;

import static models.gui.UIConstants.SCALE;

public class MainMenuBox extends VBox {
    private static final String DUELYST_LOGO_URL = "Client/resources/ui/brand_duelyst.png";
    private static final double X = 100 * SCALE;
    private static final double Y = 400 * SCALE;

    public MainMenuBox(List<MenuItem> items) throws FileNotFoundException {
        super(UIConstants.DEFAULT_SPACING * 5);
        ImageView brandView = ImageLoader.loadImage(
                DUELYST_LOGO_URL, UIConstants.DUELYST_LOGO_WIDTH, UIConstants.DUELYST_LOGO_HEIGHT
        );
        MainMenuGrid menuGrid = new MainMenuGrid(items);

        getChildren().addAll(brandView, menuGrid);
        setAlignment(Pos.CENTER);
        relocate(X, Y);
        setPadding(new Insets(UIConstants.DEFAULT_SPACING * 6));
    }
}
