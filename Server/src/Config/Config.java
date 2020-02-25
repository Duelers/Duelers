package Config;

import java.io.*;
import java.util.Properties;

public class Config{

    private static Config configInstance = null;
    private static Properties config;
    private static InputStream file = null;

    private Config(){
        try {
            file = new FileInputStream("Server/src/Config/config.properties");
            config = new Properties();
            config.load(file);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally{
            if(file != null){
                try{
                    file.close();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static Config getInstance(){
        if(configInstance == null){
            configInstance = new Config();
        }
        return configInstance;
    }

    public String getProperty(String property){
            return config.getProperty(property);
    }

}