package org.projectcardboard.client.models.localisation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageData {
  private static LanguageData languageDataInstance = null;

  private final String languageFolder = "configurations/languages";
  private final String defaultLanguage = "english";
  private final String missingValue = "???";

  private final String selectedLanguage;

  private Language languageMapDefault;
  private Language languageMapSelected;

  private static Logger logger = LoggerFactory.getLogger(LanguageData.class);

  private LanguageData(String selectedLanguage) {
    this.selectedLanguage = selectedLanguage;

    try {
      languageMapDefault = loadJson(defaultLanguage);
      languageMapSelected = loadJson(this.selectedLanguage);

    } catch (IOException e) {
      logger.warn("Could not load Language Json");
      logger.trace(e.getMessage());
    }
  }

  public static LanguageData getInstance() {
    if (languageDataInstance == null) {


      String l = "english"; // ToDo load language from configurations.
      languageDataInstance = new LanguageData(l);
    }
    return languageDataInstance;
  }

  public String getSelectedLanguage() {
    return selectedLanguage;
  }

  public String getValue(String[] keys) {
    String value = null;

    try {
      value = getValue(languageMapSelected, keys);
    } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e1) {
      logger.warn(String.format(
          "Language Localisation Error: failed to find value for keys '%s' for selected language: '%s'. Will use default value.",
          Arrays.deepToString(keys), selectedLanguage));

      // If we fail to find a translation, see if we can add default text.
      try {
        value = getValue(languageMapDefault, keys);
      } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e2) {
        logger.error(String.format(
            "Language Localisation Error: failed to find value for keys '%s' for DEFAULT language: '%s'. Inserting '%s'",
            Arrays.deepToString(keys), defaultLanguage, missingValue));
      }
    }
    return (value != null) ? value : missingValue;
  }

  private String getValue(Object languageData, String[] keys)
      throws NoSuchFieldException, IllegalAccessException, NullPointerException {

    // Tries to use reflection to convert string keys into Fields.
    // E.g: Calling this function with keys ["LOGIN_MENU", "WELCOME_MESSAGE"] should
    // be equivalent to: obj.LOGIN_MENU.WELCOME_MESSAGE
    Object obj = languageData;
    for (String key : keys) {
      Field field = obj.getClass().getDeclaredField(key);
      obj = field.get(obj);
    }
    return obj.toString();
  }

  private Language loadJson(String language) throws IOException {
    ClassLoader classLoader = LanguageData.class.getClassLoader();
    String filename = language + ".json";
    String filepath = languageFolder + "/" + filename;
    InputStream languageResource = classLoader.getResourceAsStream(filepath);

    if (languageResource != null) {
      return new Gson().fromJson(new InputStreamReader(languageResource, "UTF-8"), Language.class);
    } else {
      throw new IOException(String.format("Failed to read language file: %s.", filepath));
    }

  }

}
