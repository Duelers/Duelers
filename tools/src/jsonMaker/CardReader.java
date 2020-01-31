package jsonMaker;

import com.google.gson.GsonBuilder;
import server.dataCenter.models.card.Card;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardReader {

    public static void main(String[] args) {
        String directory = "jsonData/spellCards";
        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    Pattern pattern = Pattern.compile("\"name\": \"(.+)\"");

                    String content = "";

                    String line = bufferedReader.readLine();

                    while (line != null) {
                        content = content + line + System.lineSeparator();
                        line = bufferedReader.readLine();
                    }
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find() && !content.contains("cardId")) {
                        String name = matcher.group(1);
                        content = content.replace("\"name\": \"" + name + "\"", "\"name\": \"" + name + "\"," + System.lineSeparator() + "  \"cardId\": \"" + name.replaceAll(" ", "") + "\"");
                        System.out.println(content);
                        write(directory + "/" + file.getName(), content);
                    }


                    //Card loadedCard = new Gson().fromJson(bufferedReader, Card.class);

                    //writeAJsonFile(loadedCard, directory + "/" + file.getName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeAJsonFile(Card card, String address) {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(card);
        System.out.println(json);

        write(address, json);
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
