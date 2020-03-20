package shared.models.services;

import shared.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Log {

    private static Log logInstance = null;

    private Logger logger;
    private Boolean writeToFile = true;

    private static final String logName = Constants.LOG_NAME;
    private static final Path logPath = Constants.LOG_FILE_PATH;

    private Log(){

            try {
                if (!Files.exists(logPath)) {
                    Files.createFile(logPath);
                    System.out.println("Creating NEW log file at: " + logPath.toString());
                }
            }
            catch (IOException e){

            }

            logger = Logger.getLogger(logName);
            FileHandler fh;

            try {

                // Add file handler
                fh = new FileHandler(logPath.toString(), true);
                logger.addHandler(fh);

                // Format message: <log Level> | <datetime> | <message>
                fh.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        return record.getLevel().toString() + " | " + logTime.format(new Date()) + " | " +  record.getMessage() + "\n";
                    }
                });

                logger.setLevel(Level.ALL);

                logMsg("\n\n=====================================", Level.ALL); // Allows us to quickly tell when a new 'session' begins.

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static synchronized Log getInstance(){
        if(logInstance == null)
            logInstance = new Log();
        return logInstance;
    }

    public void logClientData(String data, Level level){
        /// use this function when the file is on the Client.
        String clientPrefix = "CLIENT | ";
        logMsg(clientPrefix + data, level);
    }

    public void logServerData(String data, Level level){
        /// Use this function when the file is on the Server.
        String serverPrefix = "SERVER | ";

        if (data != null) {
            logMsg(serverPrefix + data, level);
        }
    }

    public void logSharedData(String data, Level level){
        String serverPrefix = "SHARED | ";
        if (data != null)
            logMsg(serverPrefix + data, level);
    }

    public void logStackTrace(Exception errorMsg){
        if (errorMsg != null) {
            logMsg(errorMsg.getMessage(), Level.WARNING);
        }
    }

    public void logStackTrace(Error errorMsg){
        if (errorMsg != null) {
            logMsg(errorMsg.getMessage(), Level.WARNING);
        }
    }


    private void logMsg(String msg, Level level){
        if (writeToFile)
        {
            logger.log(level, msg);
        }

        System.out.println(msg);
    }
}
