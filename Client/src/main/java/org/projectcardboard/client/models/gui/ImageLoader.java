package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageLoader {

    public static ImageView loadImage(String url, double width, double height) throws FileNotFoundException {
        System.out.println("Loading image: " + url);
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
        System.out.println("Loading image: " + url);
        InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
        if (imageResource == null) {
            System.out.println("Failed to load image at: " + url + ". Attempting absolute path check...");
            try {
                FileInputStream inputStream = new FileInputStream(url);
                return new Image(inputStream);    
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null; //todo Instead of null, maybe return some placeholder image ?
        }
        return new Image(imageResource);
    }
}
