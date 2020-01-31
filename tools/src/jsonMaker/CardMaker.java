package jsonMaker;

import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.spell.*;
import server.gameCenter.models.map.Position;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.GsonBuilder;

public class CardMaker {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            Card card = makeNewCard();
            if (card == null) return;
            writeAJsonFile(card);
        }
    }

    private static Card makeNewCard() {
        System.out.println("name:");
        String name = scanner.nextLine();

        if (name.matches("exit")) return null;

        System.out.println("description:");
        String description = scanner.nextLine();

        System.out.println("card type: " +
                "(" +
                "0.HERO, " +
                "1.MINION, " +
                "2.SPELL, " +
                "3.FLAG, " +
                "4.USABLE_ITEM, " +
                "5.COLLECTIBLE_ITEM" +
                ")");
        CardType cardType = CardType.values()[Integer.parseInt(scanner.nextLine())];

        AttackType attackType = null;
        if (cardType == CardType.HERO || cardType == CardType.MINION) {
            System.out.println("attack type: (MELEE, RANGED, HYBRID)");
            attackType = AttackType.valueOf(scanner.nextLine());
        }

        System.out.println("number Of spells:");
        ArrayList<Spell> spells = makeSpells(Integer.parseInt(scanner.nextLine()));

        System.out.println("defaultAp:");
        int defaultAp = Integer.parseInt(scanner.nextLine());

        System.out.println("defaultHp:");
        int defaultHp = Integer.parseInt(scanner.nextLine());

        System.out.println("mannaPoint:");
        int mannaPoint = Integer.parseInt(scanner.nextLine());

        System.out.println("range:");
        int range = Integer.parseInt(scanner.nextLine());

        System.out.println("price:");
        int price = Integer.parseInt(scanner.nextLine());

        System.out.println("has combo??");
        boolean hasCombo = parseBoolean(scanner.nextLine());

        return new Card(name, description, cardType, spells, defaultAp, defaultHp, mannaPoint, price, attackType, range, hasCombo);
    }

    private static ArrayList<Spell> makeSpells(int number) {
        ArrayList<Spell> spells = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            spells.add(makeNewSpell());
        }
        return spells;
    }

    private static Spell makeNewSpell() {
        System.out.println("spell id:");
        String id = scanner.nextLine();

        System.out.println("Spell action:");
        SpellAction spellAction = makeNewSpellAction();

        System.out.println("Target:");
        Target target = makeNewTarget();

        System.out.println("Availability:");
        AvailabilityType availabilityType = makeNewAvailabilityType();

        System.out.println("coolDown:");
        int coolDown = Integer.parseInt(scanner.nextLine());

        System.out.println("mannaPoint:");
        int mannaPoint = Integer.parseInt(scanner.nextLine());

        return new Spell(id, spellAction, target, availabilityType, coolDown, mannaPoint);
    }

    private static AvailabilityType makeNewAvailabilityType() {
        System.out.println("onPut?");
        boolean onPut = parseBoolean(scanner.nextLine());

        System.out.println("onAttack?");
        boolean onAttack = parseBoolean(scanner.nextLine());

        System.out.println("onDeath?");
        boolean onDeath = parseBoolean(scanner.nextLine());

        System.out.println("continuous?");
        boolean continuous = parseBoolean(scanner.nextLine());

        System.out.println("specialPower?");
        boolean specialPower = parseBoolean(scanner.nextLine());

        System.out.println("onStart?");
        boolean onStart = parseBoolean(scanner.nextLine());

        return new AvailabilityType(onPut, onAttack, onDeath, continuous, specialPower, onStart);
    }

    private static SpellAction makeNewSpellAction() {
        System.out.println("enemyHitChanges:");
        int enemyHitChanges = Integer.parseInt(scanner.nextLine());

        System.out.println("apChange:");
        int apChange = Integer.parseInt(scanner.nextLine());

        System.out.println("hpChange:");
        int hpChange = Integer.parseInt(scanner.nextLine());

        System.out.println("mpChange:");
        int mpChange = Integer.parseInt(scanner.nextLine());

        System.out.println("poison?");
        boolean poison = parseBoolean(scanner.nextLine());

        System.out.println("makeStun?");
        boolean makeStun = parseBoolean(scanner.nextLine());

        System.out.println("makeDisarm?");
        boolean makeDisarm = parseBoolean(scanner.nextLine());

        System.out.println("actionAtTheEndOfTurn?");
        boolean actionAtTheEndOfTurn = parseBoolean(scanner.nextLine());

        System.out.println("noDisarm?");
        boolean noDisarm = parseBoolean(scanner.nextLine());

        System.out.println("noPoison?");
        boolean noPoison = parseBoolean(scanner.nextLine());

        System.out.println("noStun?");
        boolean noStun = parseBoolean(scanner.nextLine());

        System.out.println("noBadEffect?");
        boolean noBadEffect = parseBoolean(scanner.nextLine());

        System.out.println("noAttackFromWeakerOnes?");
        boolean noAttackFromWeakerOnes = parseBoolean(scanner.nextLine());

        System.out.println("disableHolyBuff?");
        boolean disableHolyBuff = parseBoolean(scanner.nextLine());

        System.out.println("addSpell?");
        boolean addSpell = parseBoolean(scanner.nextLine());

        System.out.println("killsTarget?");
        boolean killsTarget = parseBoolean(scanner.nextLine());

        System.out.println("isForGladiator?");
        boolean isForGladiator = parseBoolean(scanner.nextLine());

        System.out.println("durable?");
        boolean durable = parseBoolean(scanner.nextLine());

        System.out.println("removeBuffs:");
        int removeBuffs = Integer.parseInt(scanner.nextLine());

        System.out.println("duration:");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.println("delay:");
        int delay = Integer.parseInt(scanner.nextLine());

        Spell carryingSpell = null;
        if (addSpell) {
            System.out.println("carryingSpell:");
            carryingSpell = makeNewSpell();
        }

        return null;// new SpellAction(enemyHitChanges, apChange, hpChange, mpChange, poison, makeStun, makeDisarm, actionAtTheEndOfTurn, noDisarm, noPoison, noStun, noBadEffect, noAttackFromWeakerOnes, disableHolyBuff, addSpell, killsTarget, isForGladiator, durable, removeBuffs, duration, delay, carryingSpell);
    }

    private static Target makeNewTarget() {
        System.out.println("is related to card owner position?");
        boolean isRelatedToCardOwnerPosition = parseBoolean(scanner.nextLine());

        System.out.println("is for around own hero?");
        boolean isForAroundOwnHero = parseBoolean(scanner.nextLine());

        System.out.println("dimensions??");
        Position dimensions = new Position(
                Integer.parseInt(scanner.nextLine()), Integer.parseInt(scanner.nextLine())
        );

        System.out.println("is random?");
        boolean isRandom = parseBoolean(scanner.nextLine());

        System.out.println("is for own?");
        boolean own = parseBoolean(scanner.nextLine());

        System.out.println("is for enemy?");
        boolean enemy = parseBoolean(scanner.nextLine());

        Owner owner = new Owner(own, enemy);

        System.out.println("is for cell?");
        boolean cell = parseBoolean(scanner.nextLine());

        System.out.println("is for hero?");
        boolean hero = parseBoolean(scanner.nextLine());

        System.out.println("is for minion?");
        boolean minion = parseBoolean(scanner.nextLine());

        TargetCardType cardType = new TargetCardType(cell, hero, minion, false);

        System.out.println("is for melee?");
        boolean melee = parseBoolean(scanner.nextLine());

        System.out.println("is for ranged?");
        boolean ranged = parseBoolean(scanner.nextLine());

        System.out.println("is for hybrid?");
        boolean hybrid = parseBoolean(scanner.nextLine());

        CardAttackType attackType = new CardAttackType(melee, ranged, hybrid);

        System.out.println("is for deck cards?");
        boolean isForDeckCards = parseBoolean(scanner.nextLine());

        return new Target(isRelatedToCardOwnerPosition, isForAroundOwnHero, dimensions, isRandom, owner, cardType, attackType, isForDeckCards);
    }

    private static boolean parseBoolean(String num) {
        return Integer.parseInt(num) != 0;
    }

    private static void writeAJsonFile(Card card) {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(card);
        System.out.println(json);

        try {
            FileWriter writer = new FileWriter("address/" + card.getName() + ".format.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
