import Config.Config;
import com.google.gson.Gson;
import controller.Client;
import controller.GraphicalUserInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import models.LoadLanguage;
import models.languageLocalisation.Language;
import server.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    public static void main(String[] args){


        String hostServer = Config.getInstance().getProperty("HOST_SERVER");
        boolean shouldHostServer = Boolean.parseBoolean(hostServer);
        if(shouldHostServer) {
            System.out.println("Launching Server...");
            Server.start();
        }

        Client.getInstance().makeConnection();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GraphicalUserInterface.getInstance().start(stage);
    }
}
