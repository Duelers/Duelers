package org.projectcardboard.client.models.message;

import java.util.ArrayList;

public class GameAnimations {
    private final ArrayList<CardAnimation> attacks = new ArrayList<>();
    private final ArrayList<CardAnimation> counterAttacks = new ArrayList<>();
    private final ArrayList<SpellAnimation> spellAnimations = new ArrayList<>();

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
