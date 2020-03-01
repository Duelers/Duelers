import Config.Config;
import controller.Client;
import controller.GraphicalUserInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import models.languageLocalisation.LanguageData;
import server.GameServer;


public class Main extends Application {

    public static void main(String[] args){

        LanguageData.getInstance(); // Initialise

        String hostServer = Config.getInstance().getProperty("HOST_SERVER");
        boolean shouldHostServer = Boolean.parseBoolean(hostServer);
        if(shouldHostServer) {
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
