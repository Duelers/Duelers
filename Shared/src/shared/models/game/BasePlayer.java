package shared.models.game;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class BasePlayer<
        CardType extends Card,
        TroopType extends Troop> {
    private final String userName;
    private final int currentMP;
    private final ArrayList<CardType> hand = new ArrayList<>();
    private final ArrayList<CardType> graveyard = new ArrayList<>();
    private final CardType nextCard;
    private final int playerNumber;
    private List<TroopType> troops = new ArrayList<>();
    private TroopType hero;

    public BasePlayer(String userName, int currentMP,
                      List<CardType> hand, List<CardType> graveyard, CardType nextCard,
                      int playerNumber,
                      List<TroopType> troops,
                      TroopType hero) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand.addAll(hand);
        this.graveyard.addAll(graveyard);
        this.nextCard = nextCard;
        this.playerNumber = playerNumber;
        this.troops.addAll(troops);
        this.hero = hero;
    }

}
