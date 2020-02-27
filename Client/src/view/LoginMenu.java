package view;

import controller.GraphicalUserInterface;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.gui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import static models.gui.UIConstants.SCALE;

public class LoginMenu extends Show {

    public LoginMenu() {
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            DefaultContainer container = new DefaultContainer(new LoginMenuContainer());

            AnchorPane sceneContents = new AnchorPane(background, container);

            root.getChildren().addAll(sceneContents);


            String versionInfo = getVersionInfo();
            if (versionInfo != null) {
                DefaultLabel versionLabel = new DefaultLabel(String.format("Version: %s", versionInfo), Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.WHITE);
                root.getChildren().add(versionLabel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersionInfo() throws IOException {
        final String versionPath = "./resources/version.txt";

         FileInputStream file = new FileInputStream(versionPath);
         Scanner scanner =new Scanner(file);
         String versionInfo = scanner.nextLine();
         file.close();

         return versionInfo;
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().stopBackgroundMusic();
    }
}