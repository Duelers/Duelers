package org.projectcardboard.client.models.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {
  static Logger logger = LoggerFactory.getLogger(ImageLoader.class);

  public static ImageView loadImage(String url, double width, double height)
      throws FileNotFoundException {
    System.out.println("Loading image: " + url);
    InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
    if (imageResource == null) {
      throw new FileNotFoundException();
    }
    return makeImageView(new Image(imageResource), width, height);
  }

  static ImageView loadImage(String url, double width, double height, double x, double y)
      throws FileNotFoundException {
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
    System.out.println("Loading image: " + url);
    InputStream imageResource = ImageLoader.class.getResourceAsStream(url);
    if (imageResource == null) {
      logger.warn("Failed to load image at: " + url);
      try {
        FileInputStream inputStream = new FileInputStream(url);
        return new Image(inputStream);
      } catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
    }
    return new Image(imageResource);
  }
}
