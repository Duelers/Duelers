package Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

public class Config {

  private static Config configInstance;
  private static Properties config;
  private static InputStream file = null;
  private static Path customCardsPath;
  private static Path customCardSpritesPath;
  private static Path configFullPath;

  private Config() {
    /**
     * Attempt to get the user's config from their system's config location. If that fails, copy the
     * default out of the JAR to that config location and then load it.
     */
    try {
      AppDirs appDirs = AppDirsFactory.getInstance();
      String configDirPath = appDirs.getUserConfigDir("cardboard", "1.0", "projectcardboard", true);
      String configFileName = "/config.properties";
      configFullPath = Path.of(configDirPath + configFileName);
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
      createCustomCardsDirectory();
      createCustomCardSpritesDirectory();
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

  public Path getCustomCardsPath() {
    return customCardsPath;
  }

  public Path getCustomCardSpritesPath() {
    return customCardSpritesPath;
  }

  private void createCustomCardsDirectory() {
    try {
      customCardsPath = Path.of(configFullPath.getParent().toString() + "/Custom_Cards/", "\\");
      Files.createDirectories(customCardsPath);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void createCustomCardSpritesDirectory() {
    try {
      customCardSpritesPath =
          Path.of(configFullPath.getParent().toString() + "/Custom_Cards_Sprites/", "\\");
      Files.createDirectories(customCardSpritesPath);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
