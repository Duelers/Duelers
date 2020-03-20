package org.projectcardboard.client.view;

import Config.Config;
import org.projectcardboard.client.controller.GraphicalUserInterface;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.projectcardboard.client.models.gui.*;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;
import shared.models.services.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class LoginMenu extends Show {

    public LoginMenu() {
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            DefaultContainer container = new DefaultContainer(new LoginMenuContainer());

            AnchorPane sceneContents = new AnchorPane(background, container);

            root.getChildren().addAll(sceneContents);

            String versionInfo = getClientVersionInfo();
            if (versionInfo != null) {
                String version = LanguageData.getInstance().getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.VERSION});
                DefaultLabel versionLabel = new DefaultLabel(String.format("%s: %s", version, versionInfo), Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.DARKGRAY, 10, 10);
                root.getChildren().add(versionLabel);
            }


            String serverName = getServerInfo();
            if (serverName != null) {
                String ServerNameFullString = String.format("server: %s", serverName);
                DefaultLabel ServerLabel = new DefaultLabel(ServerNameFullString, Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.DARKGRAY, 10, 30);
                root.getChildren().add(ServerLabel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Todo move this function out of login menu, add call to Log in main
    public String getServerInfo(){

        // Todo (1) add localisation. (2) Only reveal last bit of server. eg "test3" instead of "wss://mechaz.org/test3"
        Boolean isLocalConnection = Boolean.parseBoolean(Config.getInstance().getProperty("HOST_SERVER"));
        if (isLocalConnection){
            return "local host";
        }

        return Config.getInstance().getProperty("SERVER_URI");
    }

    // Todo move this function out of login menu, add call to Log in main
    public String getClientVersionInfo() {
        final Path versionPath = Paths.get("../clientVersion.txt").toAbsolutePath();

        try {
            InputStream file = this.getClass().getResourceAsStream(versionPath.toString());
            Scanner scanner = new Scanner(file);
            String clientVersionInfo = scanner.nextLine();
            scanner.close();
            file.close();

            return clientVersionInfo;
        } catch (IOException e) {
            Log.getInstance().logClientData("Failed to find path: " + versionPath.toString(), Level.WARNING);
            Log.getInstance().logStackTrace(e);

            return "???";
        }
    }


    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().stopBackgroundMusic();
    }
}