package plistConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import plistConverter.converters.Converter;
import plistConverter.converters.PlistToFxPlaylistConverter;
import plistConverter.converters.PlistToIconPlaylistConverter;
import plistConverter.converters.PlistToTroopPlaylistConverter;
import plistConverter.models.old.Plist;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type listType = new TypeToken<ArrayList<Plist>>() {}.getType();

    public static void main(String[] args) {
        Converter converter;
        while (true) {
            System.out.println("Troop|Icon|Fx|poweroff");

            loop:
            while (true) {
                switch (scanner.nextLine()) {
                    case "Troop":
                        converter = new PlistToTroopPlaylistConverter();
                        break loop;
                    case "Icon":
                        converter = new PlistToIconPlaylistConverter();
                        break loop;
                    case "Fx":
                        converter = new PlistToFxPlaylistConverter();
                        break loop;
                    case "poweroff":
                        return;
                    default:
                        System.out.println("Error!");
                }
            }

            File directory = new File(converter.getPath() + converter.getInitialFolder());
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    try {
                        ArrayList<Plist> list = gson.fromJson(new FileReader(file), listType);

                        write(
                                converter.getPath() + converter.getDestinationFolder() + "/" + file.getName(),
                                gson.toJson(converter.convert(list.get(0)))
                        );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
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
