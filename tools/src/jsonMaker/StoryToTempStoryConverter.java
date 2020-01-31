package jsonMaker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.gameCenter.models.game.Story;
import server.gameCenter.models.game.TempStory;

import java.io.*;

public class StoryToTempStoryConverter {
    public static void main(String[] args) {
        String directory = "jsonData/stories";
        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                    Story story = new Gson().fromJson(bufferedReader, Story.class);
                    TempStory tempStory = new TempStory(story);

                    String string = new GsonBuilder().setPrettyPrinting().create().toJson(tempStory);
                    string = string.replaceAll("(First|Second|Third)Stage_", "").replaceAll("_\\d", "");
                    System.out.println(string);

                    write(directory + "/" + file.getName(), string);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void write(String address, String json) {
        try {
            FileWriter writer = new FileWriter(address);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
