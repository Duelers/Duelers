package org.projectcardboard.client;

import org.projectcardboard.client.controller.Client;
import org.projectcardboard.client.controller.GraphicalUserInterface;
import org.projectcardboard.client.models.localisation.LanguageData;

import Config.Config;
import javafx.application.Application;
import javafx.stage.Stage;
import server.GameServer;
import shared.HelperMethods;
import shared.models.services.Log;

import java.util.logging.Level;

public class Main extends Application {

    public static void main(String[] args) {

        // Initialise Singletons...
        LanguageData.getInstance();
        Log.getInstance().logClientData("\n\nStarting Client...", Level.INFO);

        String hostServer = Config.getInstance().getProperty("HOST_SERVER");
        boolean shouldHostServer = Boolean.parseBoolean(hostServer);
        if (shouldHostServer) {
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
