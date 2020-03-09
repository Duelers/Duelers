package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageLoader {

    public static ImageView loadImage(String url, double width, double height) throws FileNotFoundException {
        InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
        System.out.println(url);
        if (imageResource == null) { throw new FileNotFoundException(); }
        return makeImageView(new Image(imageResource), width, height);
    }

    static ImageView loadImage(String url, double width, double height, double x, double y) throws FileNotFoundException {
        ImageView view = loadImage(url, width, height);
        view.relocate(x, y);
        return view;
    }

    public static ImageView makeImageView(Image image, double width, double height) {
        ImageView menuView = new ImageView(image);
        menuView.setFitWidth(width);
        menuView.setFitHeight(height);
        return menuView;
    }

    public static Image load(String url) {
        try {
            return new Image(new FileInputStream(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
