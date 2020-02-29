import Config.Config;
import com.google.gson.Gson;
import controller.Client;
import controller.GraphicalUserInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import models.LoadLanguage;
import models.languages.Language;
import server.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        LoadLanguage x = new LoadLanguage("english");
        x.getValue(new String[] {"LOGIN_MENU", "LOGIN"});

        //String restaurantJson = "{'LANGUAGE_ENGLISH_TITLE':'english','LANGUAGE_NATIVE_TITLE':'english','LOGIN_MENU':{'WELCOME_MESSAGE':'The Community tries to save Duelyst.','VERSION':'version','LOGIN':'login','REGISTER':'register','PASSWORD':'password','ERROR_SHORT_PASSWORD':'Error! Password is too short.','ERROR_SHORT_USERNAME':'Error! Username is too short.','ERROR_INCORRECT_PASSWORD':'Error! Incorrect Password.'},'BUTTON_TEXT':{'OK':'ok','CANCEL':'cancel'}}";

        //String restaurantJson = new FileReader( "resources/configurations/Languages/english.json");

        Gson gson = new Gson();

        Language restaurantObject = gson.fromJson(new FileReader( "resources/configurations/Languages/english.json"), Language.class);

        String o = restaurantObject.LANGUAGE_ENGLISH_TITLE;

        String y = restaurantObject.BUTTON_TEXT.OK;


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
