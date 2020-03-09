package org.projectcardboard.client.view;

import org.projectcardboard.client.controller.GraphicalUserInterface;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.projectcardboard.client.models.gui.*;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

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
                String version = LanguageData.getInstance().getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.VERSION});
                DefaultLabel versionLabel = new DefaultLabel(String.format("%s: %s", version, versionInfo), Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.WHITE);
                root.getChildren().add(versionLabel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersionInfo() throws IOException {
        final String versionPath = "/version.txt";

         InputStream file = this.getClass().getResourceAsStream(versionPath);
         Scanner scanner = new Scanner(file);
         String versionInfo = scanner.nextLine();
         scanner.close();
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