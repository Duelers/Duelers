package shared.models.services;

import java.util.logging.*;

public class Log {

    private static Log log = null;

    private final String logName = "";

    private Log(){
        Logger logger = Logger.getLogger("myLogger");
    }

    public static synchronized Logger getInstance(){
        if(log == null)
            log = new Log();
        return log;
    }



    private String getTime(){

    }

    private String getDDMMYYY(){

    }

}
