package models.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageLoader {

    public static ImageView loadImage(String url, double width, double height) throws FileNotFoundException {
        return makeImageView(new Image(new FileInputStream(url)), width, height);
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
