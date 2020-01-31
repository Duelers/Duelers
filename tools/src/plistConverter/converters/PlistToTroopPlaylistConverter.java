package plistConverter.converters;

import plistConverter.models.newer.Frame;
import plistConverter.models.newer.Playlist;
import plistConverter.models.old.Plist;
import plistConverter.models.old.PlistFrame;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlistToTroopPlaylistConverter implements Converter<Playlist> {
    private static final Pattern framePattern = Pattern.compile("(\\d+),(\\d+)");

    public Playlist convert(Plist plist) {
        Playlist troopPlaylist = new Playlist();
        ArrayList<Frame> attack = new ArrayList<>();
        ArrayList<Frame> breathing = new ArrayList<>();
        ArrayList<Frame> death = new ArrayList<>();
        ArrayList<Frame> hit = new ArrayList<>();
        ArrayList<Frame> idle = new ArrayList<>();
        ArrayList<Frame> run = new ArrayList<>();

        troopPlaylist.lists.put("attack", attack);
        troopPlaylist.lists.put("breathing", breathing);
        troopPlaylist.lists.put("death", death);
        troopPlaylist.lists.put("hit", hit);
        troopPlaylist.lists.put("idle", idle);
        troopPlaylist.lists.put("run", run);

        for (Entry<String, PlistFrame> entry : plist.frames.entrySet()) {
            PlistFrame plistFrame = entry.getValue();

            Matcher matcher = framePattern.matcher(plistFrame.frame);
            matcher.find();
            Frame frame = new Frame(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))
            );
            if (entry.getKey().contains("attack")) {
                attack.add(frame);
            } else if (entry.getKey().contains("breathing")) {
                breathing.add(frame);
            } else if (entry.getKey().contains("death")) {
                death.add(frame);
            } else if (entry.getKey().contains("hit")) {
                hit.add(frame);
            } else if (entry.getKey().contains("idle")) {
                idle.add(frame);
            } else if (entry.getKey().contains("run")) {
                run.add(frame);
            }

            matcher.find();
            troopPlaylist.frameWidth = Integer.parseInt(matcher.group(1));
            troopPlaylist.frameHeight = Integer.parseInt(matcher.group(2));
        }
        return troopPlaylist;
    }

    @Override
    public String getPath() {
        return "../resources/units";
    }

    @Override
    public String getInitialFolder() {
        return "/initialJsons";
    }

    @Override
    public String getDestinationFolder() {
        return "/finalJsons";
    }
}
