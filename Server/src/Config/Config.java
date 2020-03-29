package Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

public class Config {

  private static Config configInstance = null;
  private static Properties config;
  private static InputStream file = null;
  private static final String configFileName = "/config.properties";

  private Config() {
    /**
     * Attempt to get the user's config from their system's config location. If that fails, copy the
     * default out of the JAR to that config location and then load it.
     */
    try {
      AppDirs appDirs = AppDirsFactory.getInstance();
      String configDirPath = appDirs.getUserConfigDir("cardboard", "1.0", "projectcardboard", true);
      Path configFullPath = Path.of(configDirPath + configFileName);
      try {
        file = new FileInputStream(configFullPath.toString());
      } catch (FileNotFoundException e) {
        InputStream defaultConfig = Config.class.getResourceAsStream(configFileName);
        if (defaultConfig == null) {
          throw new IOException("Couldn't find default configuration at " + configFileName);
        }
        Files.createDirectories(configFullPath.getParent());
        Files.copy(defaultConfig, configFullPath);
        file = new FileInputStream(configFullPath.toString());
      }

      config = new Properties();
      config.load(file);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (file != null) {
        try {
          file.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public static Config getInstance() {
    if (configInstance == null) {
      configInstance = new Config();
    }
    return configInstance;
  }

  public String getProperty(String property) {
    return config.getProperty(property);
  }

  private Properties loadDefaultConfigFile() {
    Properties defaultProperties = null;

    try {
      InputStream defaultConfig = Config.class.getResourceAsStream(configFileName);
      if (defaultConfig == null) {
        throw new FileNotFoundException("Couldn't find default configuration at " + configFileName);
      }
      defaultProperties = new Properties();
      defaultProperties.load(defaultConfig);

      defaultConfig.close();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return defaultProperties;
  }

  public boolean shouldUpdateUserConfig() {

    try {
      Properties defaultConfig = loadDefaultConfigFile();
      Set<String> defaultVariables = defaultConfig.stringPropertyNames();
      Set<String> userVariables = config.stringPropertyNames();
      boolean notRunningLocalHost = config.getProperty("HOST_SERVER").equalsIgnoreCase("false");
      if (!defaultVariables.equals(userVariables) && notRunningLocalHost) {
        return true;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return false;
  }

  public void updateUserConfig() {

    Properties defaultConfig = loadDefaultConfigFile();
    defaultConfig.forEach((key, value) -> {
      if (!config.containsKey(key.toString())) {
        config.put(key.toString(), value.toString());
        System.out.println("User did not have variable " + key.toString() + ". Adding...");
      }
    });

    try {
      AppDirs appDirs = AppDirsFactory.getInstance();
      String configDirPath = appDirs.getUserConfigDir("cardboard", "1.0", "projectcardboard", true);
      Path configFullPath = Path.of(configDirPath + configFileName);
      OutputStream out = new FileOutputStream(configFullPath.toString(), false);
      String comment =
          "#WARNING: IF YOURE GOING TO ADD NEW VARIABLES DO NOT ADD SPACES AROUND THE EQUALS(=) SIGN\n"
              + "#WARNING: DO NOT EDIT FOR LOCAL CHANGES\n"
              + "#This will be copied to your system's config location on first run. If you want to change it, change it there.\n"
              + "#MacOSX: /Users/yourusername/Library/Application Support/cardboard/1.0\n"
              + "#Windows: C:\\Users\\yourusername\\AppData\\Roaming\\projectcardboard\\cardboard\\1.0\n"
              + "#Linux: /home/yourusername/.config/cardboard/1.0\n";
      config.store(out, comment);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
