package org.projectcardboard.client.models.card;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.projectcardboard.client.Constants;
import org.projectcardboard.client.models.JsonConverter;

public class DeckExporter {
  private static final String DIRECTORY_NAME = Constants.DECK_EXPORT_FOLDER;
  private static final String FORMAT = ".deck.json";
  private final ExportedDeck deck;

  public DeckExporter(Deck deck) {
    this.deck = new ExportedDeck(deck);
  }

  public void export() {
    File directory = new File(DIRECTORY_NAME);
    if (!directory.exists()) {
      directory.mkdir();
    }
    File[] files = directory.listFiles();
    if (files != null) {
      int counter = 1;
      String name = deck.getName();
      outer: while (files.length > 0) { // todo: This is definitely written wrong. Test it so we can
                                        // safely change it.
        for (File file : files) {
          if (file.getName().equals(name + FORMAT)) {
            counter++;
            name = deck.getName() + counter;
            continue outer;
          }
        }
        break;
      }
      try {
        FileWriter writer = new FileWriter(new File(DIRECTORY_NAME + "/" + name + FORMAT));
        writer.write(JsonConverter.toJson(deck));
        writer.flush();
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
