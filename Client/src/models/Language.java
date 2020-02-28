package models;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;


public class Language {
    private final String languageFolder = "languages/configurations/Languages";

    private final String selectedLanguage;

    private JSONObject languageMapEnglish;
    private JSONObject languageMapSelected;


    public Language(String selectedLanguage){
        this.selectedLanguage = selectedLanguage;

        try {
            languageMapEnglish = loadJson("english");
            languageMapSelected = loadJson(this.selectedLanguage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key){
        String value;
        try {
            value = languageMapSelected.get(key).toString();
        }
        catch (JSONException e){
            System.out.println(String.format("Error, failed to find key '%s' for selected language: '%s'", key, selectedLanguage));
            value = languageMapEnglish.get(key).toString();
        }
        return value;
    }

    public String getValue(String key1, String key2){
        String value;
        try {
            value = languageMapSelected.getJSONObject(key1).get(key2).toString();
        }
        catch (JSONException e){
            System.out.println(String.format("Error, failed to find key '%s' with subkey '%s' for selected language: '%s'", key1, key2, selectedLanguage));
            value = languageMapEnglish.getJSONObject(key1).get(key2).toString();
        }
        return value;
    }

    private JSONObject loadJson(String language) throws FileNotFoundException {
        String filename = language + ".json";
        String filepath = languageFolder + "/" + filename;

        JSONObject loadedLanguage = new JSONObject(filepath);

        return loadedLanguage;
    }
}
