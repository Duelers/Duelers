package org.projectcardboard.client;

import org.projectcardboard.client.controller.Client;
import org.projectcardboard.client.controller.GraphicalUserInterface;
import org.projectcardboard.client.models.localisation.LanguageData;

import Config.Config;
import javafx.application.Application;
import javafx.stage.Stage;
import server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    logger.info("Starting Client with the following args: '" + String.join("", args) + "'");

    LanguageData.getInstance(); // Initialise

    String hostServer = Config.getInstance().getProperty("HOST_SERVER");
    boolean shouldHostServer = Boolean.parseBoolean(hostServer);
    if (shouldHostServer) {
      logger.info("'Local Host Mode' set to: True. Starting GameServer...");
      GameServer.start();
    }
    Client.getInstance().makeConnection();

    // Todo add logging calls to get client and server versions.
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    GraphicalUserInterface.getInstance().start(stage);
  }
}
