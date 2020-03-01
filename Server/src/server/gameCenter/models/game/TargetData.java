package server.gameCenter.models.game;

import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

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

    public Set<Cell> getPositions() {
        Set<Cell> cells = this.cells.stream().map(n -> new Cell(n.getRow(), n.getColumn())).collect(Collectors.toSet());
        cells.addAll(troops.stream().map(Troop::getCell).map(n -> new Cell(n.getRow(), n.getColumn())).collect(Collectors.toSet()));
        return Collections.unmodifiableSet(cells);
    }
}
