package view.BattleView;

import com.google.gson.Gson;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import models.gui.ImageLoader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static view.BattleView.CardAnimation.cachedImages;

public class SpellAnimation extends Transition {
    private final FramePosition[] framePositions;

    private final ImageView imageView;
    private final int frameWidth, frameHeight;

    private boolean frameShowFlag = false;
    private int nextIndex = 0;
    private Group mapGroup;

    SpellAnimation(Group mapGroup, String fileName, double x, double y) throws Exception {
        Playlist playlist = new Gson().fromJson(new FileReader("Client/resources/fx/" + fileName + ".plist.json"), Playlist.class);
        framePositions = playlist.getFrames();

        frameWidth = playlist.frameWidth;
        frameHeight = playlist.frameHeight;
        setCycleDuration(Duration.millis(playlist.frameDuration));

        Image image = cachedImages.computeIfAbsent(fileName, key -> ImageLoader.load("Client/resources/fx/" + fileName + ".png"));
        imageView = new ImageView(image);

        imageView.setFitWidth(frameWidth * Constants.SPELL_SCALE * Constants.SCALE);
        imageView.setFitHeight(frameHeight * Constants.SPELL_SCALE * Constants.SCALE);
        imageView.setX(x - playlist.extraX * Constants.SCALE);
        imageView.setY(y - playlist.extraY * Constants.SCALE);
        imageView.setViewport(new Rectangle2D(0, 0, 1, 1));

        this.setCycleCount(INDEFINITE);

        this.mapGroup = mapGroup;
        mapGroup.getChildren().add(imageView);
        this.play();
    }

    @Override
    protected void interpolate(double v) {
        if (!frameShowFlag && v < 0.5)
            frameShowFlag = true;
        if (frameShowFlag && v > 0.5) {
            imageView.setViewport(new Rectangle2D(framePositions[nextIndex].x,
                    framePositions[nextIndex].y, frameWidth, frameHeight));
            nextIndex++;
            if (nextIndex == framePositions.length) {
                nextIndex = 0;
                mapGroup.getChildren().remove(imageView);
            }
            frameShowFlag = false;
        }
    }

    class Playlist {
        int frameWidth;
        int frameHeight;
        int frameDuration;
        double extraX;
        double extraY;
        private HashMap<String, ArrayList<FramePosition>> lists = new HashMap<>();

        FramePosition[] getFrames() {
            return lists.get("frames").toArray(new FramePosition[1]);
        }
    }

    class FramePosition {
        final double x;
        final double y;

        FramePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
