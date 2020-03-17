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

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


            // Todo add Localization of 'server',  'local host'
            String uri = Config.getInstance().getProperty("SERVER_URI");
            Boolean isLocalConnection = Boolean.parseBoolean(Config.getInstance().getProperty("HOST_SERVER"));
            String ServerName = String.format("server: %s", isLocalConnection ? "local host" : uri);
            DefaultLabel ServerLabel = new DefaultLabel(ServerName, Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.DARKGRAY, 10, 30);
            root.getChildren().add(ServerLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerInfo(){
        Boolean isLocalConnection = Boolean.parseBoolean(Config.getInstance().getProperty("HOST_SERVER"));
        if (isLocalConnection){
            return "local host";
        }

        String uri = Config.getInstance().getProperty("SERVER_URI");
        Pattern p = Pattern.compile("/(\\w*$)");
        Matcher m = p.matcher(uri);
        m.matches();

        String serverName = "Production";
        try {
            serverName = m.group(0);
        }
        catch(Exception e){
            // do nothing
        }

        return serverName;
    }

    public String getClientVersionInfo() throws IOException {
        final String versionPath = "/clientVersion.txt";

         InputStream file = this.getClass().getResourceAsStream(versionPath);
         Scanner scanner = new Scanner(file);
         String clientVersionInfo = scanner.nextLine();
         scanner.close();
         file.close();

         return clientVersionInfo;
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().stopBackgroundMusic();
    }
}