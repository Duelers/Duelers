package plistConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import plistConverter.models.newer.Playlist;

import java.io.*;

public class TroopPlaylistChanger {
    private static final String path = "../Client/resources/troopAnimations/";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws FileNotFoundException {
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().contains(".json")) continue;
                if (file.getName().contains("boss_andromeda")) continue;
                if (file.getName().contains("boss_decepticlechassis")) continue;
                if (file.getName().contains("boss_chaosknight")) continue;
                Playlist playlist = gson.fromJson(new FileReader(file), Playlist.class);
                playlist.extraX = 75;
                playlist.extraY = 75;

                write(
                        path + file.getName(),
                        gson.toJson(playlist)
                );
            }
        }
    }

    private static void write(String address, String json) {
        System.out.println(json);
        try {
            FileWriter writer = new FileWriter(address);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
