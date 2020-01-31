package jsonMaker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.Deck;
import server.gameCenter.models.game.GameType;
import server.gameCenter.models.game.Story;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StoryMaker {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        for (int i = 2; i < 3; i++) {
            System.out.println("deck name:");
            String deckName = scanner.nextLine();

            System.out.println("hero name:");
            Card hero = loadCard("jsonData/heroCards/" + scanner.nextLine() + ".hero.card.json", deckName);

            System.out.println("item name:");
            Card item = loadCard("jsonData/itemCards/usable/" + scanner.nextLine() + ".usable.item.card.json", deckName);

            ArrayList<Card> others = loadOtherCards(deckName);

            System.out.println("reward:");
            int reward = Integer.parseInt(scanner.nextLine());

            writeStoryFile(new Story(new Deck(deckName, hero, item, others), GameType.values()[i], reward, 16));
            //ERROR:This deck uses same reference!
        }
    }

    private static void writeStoryFile(Story story) {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(story);

        try {
            FileWriter writer = new FileWriter("jsonData/stories/" + story.getDeck().getDeckName().replaceAll(" ", "") + ".story.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Card> loadOtherCards(String deckName) throws IOException {
        ArrayList<Card> others = new ArrayList<>();

        System.out.println("number of spells:");
        int num = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < num; i++) {
            System.out.println("spell name:");
            others.add(loadCard("jsonData/spellCards/" + scanner.nextLine() + ".spell.card.json", deckName));
        }

        for (int i = 0; i < 20 - num; i++) {
            System.out.println("minion name:");
            others.add(loadCard("jsonData/minionCards/" + scanner.nextLine() + ".minion.card.json", deckName));
        }

        return others;
    }

    private static Card loadCard(String address, String deckName) throws IOException {
        Card card = new Gson().fromJson(new BufferedReader(new FileReader(address)), Card.class);
        System.out.println("id:");
        card.setCardId(deckName.replaceAll(" ", "") + "_" + card.getName().replaceAll(" ", "") + "_" + scanner.nextLine());
        return card;
    }
}
