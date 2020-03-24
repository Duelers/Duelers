package server.clientPortal.models.message;


import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.Set;

class GameAnimations {
    private final ArrayList<CardAnimation> attacks = new ArrayList<>();
    private final ArrayList<CardAnimation> counterAttacks = new ArrayList<>();
    private final ArrayList<SpellAnimation> spellAnimations = new ArrayList<>();

    void addAttacks(String cardID, String defenderCardID) {
        attacks.add(new CardAnimation(cardID, defenderCardID));
    }

    void addCounterAttacks(String cardID, String defenderCardID) {
        counterAttacks.add(new CardAnimation(cardID, defenderCardID));
    }

    void addSpellAnimation(Set<Cell> cells, AvailabilityType availabilityType, String fxName) {
        spellAnimations.add(new SpellAnimation(cells, availabilityType, fxName));
    }
}
