package server.dataCenter.models.sorter;

import server.gameCenter.models.game.Story;

import java.util.Comparator;

public class StoriesSorter implements Comparator<Story> {
    @Override
    public int compare(Story o1, Story o2) {
        return o1.getDeck().getDeckName().compareTo(o2.getDeck().getDeckName());
    }
}