package models.languageLocalisation;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class LanguageData {
    private static LanguageData languageDataInstance = null;

    private final String languageFolder = "resources/configurations/Languages";

    private final String defaultLanguage = "english";

    private final String selectedLanguage;

    private Language languageMapDefault;
    private Language languageMapSelected;

    private LanguageData(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;

        try {
            languageMapDefault = loadJson(defaultLanguage);
            languageMapSelected = loadJson(this.selectedLanguage);

        } catch (IOException e) {
            System.out.println(String.format("Language Localisation Error! Could not find language files in dir: '%s'", languageFolder));
            e.printStackTrace();
        }
    }

    public static LanguageData getInstance(){
        if(languageDataInstance == null){

            // ToDo load language from user settings.
            String l = "korean";
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
            System.out.println(String.format("Language Error: failed to find value for keys '%s' for selected language: '%s'", Arrays.deepToString(keys), selectedLanguage));

            // If we fail to find a translation, see if we can add default text.
            try {
                value = getValue(languageMapDefault, keys);
            } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e2) {
                System.out.println(String.format("Language Error: failed to find value for keys '%s' for DEFAULT language: '%s'", Arrays.deepToString(keys), defaultLanguage));
            }
        }
        return (value != null) ? value.toString() : "Missing Language Value";
    }

    private String getValue(Object languageData, String[] keys) throws NoSuchFieldException, IllegalAccessException, NullPointerException {

        // Tries to use reflection to convert string keys into Fields.
        // E.g: Calling this function with keys ["LOGIN_MENU", "WELCOME_MESSAGE"] should be equivalent to: obj.LOGIN_MENU.WELCOME_MESSAGE
        Object obj = languageData;
        for (String key: keys){
            Field field = obj.getClass().getDeclaredField(key);
            obj = field.get(obj);
        }
        return obj.toString();
    }

    private Language  loadJson(String language) throws IOException {
        String filename = language + ".json";
        String filepath = languageFolder + "/" + filename;

        Reader reader = Files.newBufferedReader(Paths.get(filepath), StandardCharsets.UTF_8);
        Language loadedLanguage = new Gson().fromJson(reader, Language.class);

        return loadedLanguage;
    }


}
