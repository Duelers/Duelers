package localGameCenter.models.game;

import localDataCenter.models.card.Card;
import localGameCenter.models.map.Cell;
import localGameCenter.models.map.Position;

import java.util.*;
import java.util.stream.Collectors;

public class TargetData {
    private List<Card> cards = new ArrayList<>();
    private List<Troop> troops = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    TargetData(List<Troop> troops) {
        this.troops = troops;
    }

    TargetData() {
    }

    List<Card> getCards() {
        return cards;
    }

    List<Troop> getTroops() {
        return troops;
    }

    List<Cell> getCells() {
        return cells;
    }

    List<Player> getPlayers() {
        return players;
    }

    public Set<Position> getPositions() {
        Set<Position> positions = cells.stream().map(Position::new).collect(Collectors.toSet());
        positions.addAll(troops.stream().map(Troop::getCell).map(Position::new).collect(Collectors.toSet()));
        return Collections.unmodifiableSet(positions);
    }
}
