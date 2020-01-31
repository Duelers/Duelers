package plistConverter.models.newer;

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {
    public int frameWidth;
    public int frameHeight;
    int frameDuration = 100;
    public double extraX;
    public double extraY;
    public HashMap<String, ArrayList<Frame>> lists = new HashMap<>();
}
