package models.languageLocalisation;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class LoadLanguage {
    private final String languageFolder = "resources/configurations/Languages";
    private final String defaultLanguage = "english";

    private final String selectedLanguage;

    private Language languageMapDefault;
    private Language languageMapSelected;

    public LoadLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;

        try {
            languageMapDefault = loadJson(defaultLanguage);
            languageMapSelected = loadJson(this.selectedLanguage);

        } catch (IOException e) {
            System.out.println(String.format("Language Localisation Error! Could not find language files in dir: '%s'", languageFolder));
            e.printStackTrace();
        }
    }

    public String getValue(String[] keys) {
        String value = null;

        try {
            value = getValue(languageMapSelected, keys);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println(String.format("Language Error: failed to find value for keys '%s' for selected language: '%s'", keys.toString(), selectedLanguage));
            e.printStackTrace();

            // If we fail to find a translation, see if we can add default text.
            try {
                value = getValue(languageMapDefault, keys);
            } catch (IllegalAccessException | NoSuchFieldException e2) {
                System.out.println(String.format("Language Error: failed to find value for keys '%s' for selected language: '%s'", keys.toString(), selectedLanguage));
                e2.printStackTrace();
            }
        }
        return (value != null) ? value.toString() : "Missing Language Value";
    }

    private String getValue(Object languageData, String[] keys) throws NoSuchFieldException, IllegalAccessException {

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
