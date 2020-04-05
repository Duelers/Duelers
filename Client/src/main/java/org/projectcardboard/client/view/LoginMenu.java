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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class LoginMenu extends Show {

  private static Logger logger = LoggerFactory.getLogger(LoginMenu.class);

  public LoginMenu() {
    try {
      root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

      BorderPane background = BackgroundMaker.getMenuBackground();
      DefaultContainer container = new DefaultContainer(new LoginMenuContainer());

      AnchorPane sceneContents = new AnchorPane(background, container);

      root.getChildren().addAll(sceneContents);

      String versionInfo = getClientVersionInfo();
      if (versionInfo != null) {
        String version = LanguageData.getInstance()
            .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.VERSION});
        DefaultLabel versionLabel = new DefaultLabel(String.format("%s: %s", version, versionInfo),
            Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.DARKGRAY, 10, 10);
        root.getChildren().add(versionLabel);
      }


      String serverName = getServerInfo();
      if (serverName != null) {
        String ServerNameFullString = String.format("server: %s", serverName);
        DefaultLabel ServerLabel = new DefaultLabel(ServerNameFullString,
            Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE), Color.DARKGRAY, 10, 30);
        root.getChildren().add(ServerLabel);
      }

    } catch (IOException e) {
      logger.warn("error finding files in login menu");
      logger.debug(e.getMessage());
    }
  }

  public String getServerInfo() {

    // Todo (1) add localisation. (2) Only reveal last bit of server. eg "test3" instead of
    // "wss://mechaz.org/test3"
    Boolean isLocalConnection =
        Boolean.parseBoolean(Config.getInstance().getProperty("HOST_SERVER"));
    if (isLocalConnection) {
      return "local host";
    }

    return Config.getInstance().getProperty("SERVER_URI");
  }

  public String getClientVersionInfo() throws IOException { // Todo refactor to use configs.
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
