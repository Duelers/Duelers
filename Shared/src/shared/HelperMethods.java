package shared;

import shared.models.services.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;

public class HelperMethods {

    public String getClientVersionInfo(){
        final Path versionPath = Paths.get("/clientVersion.txt");

        try {
            InputStream file = this.getClass().getResourceAsStream(versionPath.toString());
            Scanner scanner = new Scanner(file);
            String clientVersionInfo = scanner.nextLine();
            scanner.close();
            file.close();

            return clientVersionInfo;
        }
        catch ( IOException e){
            Log.getInstance().logSharedData("Failed to find path: " + versionPath.toString(), Level.WARNING);
            Log.getInstance().logStackTrace(e);

            return "???";
        }
    }
}
