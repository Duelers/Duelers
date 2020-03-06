package org.projectcardboard.client.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import com.google.gson.Gson;

import org.projectcardboard.client.models.gui.ImageLoader;
import org.projectcardboard.client.view.PlayList;

import javafx.scene.image.Image;

public class ResourcesCenter {
    private final static ResourcesCenter ourInstance = new ResourcesCenter();
    private static final String PATH = "Client/src/main/resources";
    private final HashMap<String, Image> imageHashMap = new HashMap<>(); // todo These HashMaps are never read. Remove?
    private final HashMap<String, PlayList> playListHashMap = new HashMap<>();
    private final HashMap<String, byte[]> stringMediaHashMap = new HashMap<>();

    private ResourcesCenter() {
    }

    private static void readData() {
        File file = new File(PATH);
        try {
            readFile(file);
        } catch (IOException ignored) {
        }
    }

    private static void readFile(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null)
                return;
            for (File file1 : files) {
                readFile(file1);
            }
        } else {
            if (file.getName().contains(".plist.json")) {
                PlayList playlist = new Gson().fromJson(new FileReader(file), PlayList.class);
                ourInstance.playListHashMap.put(file.getPath(), playlist);
            }
            if (file.getName().contains(".png")) {
                Image x = ImageLoader.load(file.getPath());
                ourInstance.imageHashMap.put(file.getPath(), x);
            }
            if (file.getName().contains(".m4a")) {
                byte[] x = Files.readAllBytes(file.toPath());
                ourInstance.stringMediaHashMap.put(file.getPath(), x);
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(Runtime.getRuntime().totalMemory() / 1000000);
            readData();
        } catch (OutOfMemoryError e) {
            System.out.println("ho");
        }
        System.out.println(Runtime.getRuntime().totalMemory() / 1000000);

        System.out.println("x");
    }

    public static ResourcesCenter getInstance() {
        return ourInstance;
    }

}
