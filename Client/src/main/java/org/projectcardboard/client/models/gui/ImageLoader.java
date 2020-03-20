package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.models.services.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;

public class ImageLoader {

    public static ImageView loadImage(String url, double width, double height) throws FileNotFoundException {
        InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
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

    public static Image load(String url){
        InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
        if (imageResource == null) {
            Log.getInstance().logClientData("Failed to load image at: " + url, Level.WARNING);
            return null; //todo Instead of null, maybe return some placeholder image ?
        }
        return new Image(imageResource);
    }
}
