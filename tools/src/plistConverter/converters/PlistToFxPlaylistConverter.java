package plistConverter.converters;

import plistConverter.models.newer.Frame;
import plistConverter.models.newer.Playlist;
import plistConverter.models.old.Plist;
import plistConverter.models.old.PlistFrame;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlistToFxPlaylistConverter implements Converter<Playlist>{
    private final Pattern framePattern = Pattern.compile("(\\d+),(\\d+)");

    @Override
    public Playlist convert(Plist plist) {
        Playlist fxPlayList = new Playlist();
        ArrayList<Frame> frames = new ArrayList<>();
        fxPlayList.lists.put("frames", frames);
        for (Map.Entry<String, PlistFrame> entry : plist.frames.entrySet()) {
            PlistFrame plistFrame = entry.getValue();

            Matcher matcher = framePattern.matcher(plistFrame.frame);
            matcher.find();
            Frame frame = new Frame(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))
            );

            frames.add(frame);

            matcher.find();
            fxPlayList.frameWidth = Integer.parseInt(matcher.group(1));
            fxPlayList.frameHeight = Integer.parseInt(matcher.group(2));
        }
        return fxPlayList;
    }

    @Override
    public String getPath() {
        return "../resources/fx";
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
