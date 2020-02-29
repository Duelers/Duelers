package models.languageLocalisation;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;


public class LoadLanguage {
    private final String languageFolder = "resources/configurations/Languages";
    private final String defaultLanguage = "english";

    private final String selectedLanguage;

    private Language languageMapDefault;
    private Language languageMapSelected;

    public LoadLanguage(String selectedLanguage){
        this.selectedLanguage = selectedLanguage;

        try {
            languageMapDefault = loadJson(defaultLanguage);
            languageMapSelected = loadJson(this.selectedLanguage);

        } catch (FileNotFoundException e) {
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
        }

        // If we fail to find a translation, see if we can add default text.
        try {
            value = getValue(languageMapDefault, keys);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println(String.format("Language Error: failed to find value for keys '%s' for selected language: '%s'", keys.toString(), selectedLanguage));
            e.printStackTrace();
        }

        return (value != null) ? value.toString() : "Missing Language Value";
    }

    private String getValue(Object languageData, String[] keys) throws NoSuchFieldException, IllegalAccessException {
        Object obj = null;
        for (String key: keys){
            Field field = languageData.getClass().getDeclaredField(key);
            obj = field.get(languageData);

        }
        return obj.toString();
    }

    private Language  loadJson(String language) throws FileNotFoundException {
        String filename = language + ".json";
        String filepath = languageFolder + "/" + filename;

        Language loadedLanguage = new Gson().fromJson(new FileReader( filepath), Language.class);

        return loadedLanguage;
    }
}
