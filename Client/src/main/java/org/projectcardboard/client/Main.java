package org.projectcardboard.client;

import org.projectcardboard.client.controller.Client;
import org.projectcardboard.client.controller.GraphicalUserInterface;
import org.projectcardboard.client.models.localisation.LanguageData;
import Config.Config;
import javafx.application.Application;
import javafx.stage.Stage;
import server.GameServer;

public class Main extends Application {

  public static void main(String[] args) {

    LanguageData.getInstance(); // Initialise
    if (Config.getInstance().shouldUpdateUserConfig()) {
      Config.getInstance().updateUserConfig();
    }
    String hostServer = Config.getInstance().getProperty("HOST_SERVER");
    boolean shouldHostServer = Boolean.parseBoolean(hostServer);
    if (shouldHostServer) {
      System.out.println("Launching GameServer...");
      GameServer.start();
    }

    Client.getInstance().makeConnection();
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    GraphicalUserInterface.getInstance().start(stage);
  }
}
