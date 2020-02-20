package message;


import localDataCenter.models.card.spell.AvailabilityType;
import localGameCenter.models.map.Position;

import java.util.ArrayList;
import java.util.Set;

public class GameAnimations {
    private ArrayList<CardAnimation> attacks = new ArrayList<>();
    private ArrayList<CardAnimation> counterAttacks = new ArrayList<>();
    private ArrayList<SpellAnimation> spellAnimations = new ArrayList<>();

    void addAttacks(String cardID, String defenderCardID) {
        attacks.add(new CardAnimation(cardID, defenderCardID));
    }

    void addCounterAttacks(String cardID, String defenderCardID) {
        counterAttacks.add(new CardAnimation(cardID, defenderCardID));
    }

    void addSpellAnimation(Set<Position> positions, AvailabilityType availabilityType) {
        spellAnimations.add(new SpellAnimation(positions, availabilityType));
    }

    public ArrayList<CardAnimation> getAttacks() {
        return attacks;
    }

    public ArrayList<CardAnimation> getCounterAttacks() {
        return counterAttacks;
    }

    public ArrayList<SpellAnimation> getSpellAnimations() {
        return spellAnimations;
    }
}
