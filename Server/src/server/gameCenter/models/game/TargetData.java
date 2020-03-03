package server.gameCenter.models.game;

import shared.models.card.Card;
import shared.models.game.map.Cell;

import java.util.*;
import java.util.stream.Collectors;

public class TargetData {
    private List<Card> cards = new ArrayList<>();
    private List<ServerTroop> troops = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    TargetData(List<ServerTroop> troops) {
        this.troops = troops;
    }

    TargetData() {
    }

    List<Card> getCards() {
        return cards;
    }

    List<ServerTroop> getTroops() {
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
        cells.addAll(troops.stream().map(ServerTroop::getCell).map(n -> new Cell(n.getRow(), n.getColumn())).collect(Collectors.toSet()));
        return Collections.unmodifiableSet(cells);
    }
}
