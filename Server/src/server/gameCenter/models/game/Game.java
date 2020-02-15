package server.gameCenter.models.game;

import server.Server;
import server.clientPortal.models.comperessedData.CompressedGame;
import server.clientPortal.models.message.CardPosition;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.MatchHistory;
import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.spell.Spell;
import server.dataCenter.models.card.spell.SpellAction;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.exceptions.ServerException;
import server.gameCenter.GameCenter;
import server.gameCenter.models.game.availableActions.Attack;
import server.gameCenter.models.game.availableActions.AvailableActions;
import server.gameCenter.models.game.availableActions.Insert;
import server.gameCenter.models.game.availableActions.Move;
import server.gameCenter.models.map.Cell;
import server.gameCenter.models.map.GameMap;
import server.gameCenter.models.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Game {
    private static final int DEFAULT_REWARD = 1000;
    private static final long TURN_TIME_LIMIT = 60000;
    private Player playerOne;
    private Player playerTwo;
    private GameType gameType;
    private ArrayList<Buff> buffs = new ArrayList<>();
    private ArrayList<Buff> tempBuffs = new ArrayList<>();
    private GameMap gameMap;
    private int turnNumber = 1;
    private int reward;
    private boolean isFinished;
    private ArrayList<Account> observers = new ArrayList<>();

    protected Game(Account account, Deck secondDeck, String userName, GameMap gameMap, GameType gameType) {
        this.gameType = gameType;
        this.gameMap = gameMap;
        this.playerOne = new Player(account.getMainDeck(), account.getUsername(), 1);
        this.playerTwo = new Player(secondDeck, userName, 2);
    }

    public static int getDefaultReward() {
        return DEFAULT_REWARD;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void startGame() {
        playerOne.setCurrentMP(2);

        applyOnStartSpells(playerOne.getDeck());
        putMinion(1, playerOne.createHero(), gameMap.getCell(2, 0));

        this.turnNumber = 2;
        applyOnStartSpells(playerTwo.getDeck());
        putMinion(2, playerTwo.createHero(), gameMap.getCell(2, 8));

        this.turnNumber = 1;

        startTurnTimeLimit();

        Server.getInstance().sendGameUpdateMessage(this);
    }

    public CompressedGame toCompressedGame() {
        return new CompressedGame(playerOne, playerTwo, gameMap, turnNumber, gameType);
    }

    private void applyOnStartSpells(Deck deck) {
        for (Card card : deck.getOthers()) {
            iterateOnOnStartSpells(card);
        }
        if (deck.getItem() != null) {
            iterateOnOnStartSpells(deck.getItem());
        }
        if (deck.getHero() != null) {
            iterateOnOnStartSpells(deck.getHero());
        }
    }

    private void iterateOnOnStartSpells(Card card) {
        for (Spell spell : card.getSpells()) {
            if (spell.getAvailabilityType().isOnStart())
                applySpell(spell, detectTarget(
                        spell,
                        gameMap.getCell(2, 2),
                        gameMap.getCell(2, 2),
                        gameMap.getCell(2, 2))
                );
        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getCurrentTurnPlayer() {
        if (turnNumber % 2 == 1) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    public Player getOtherTurnPlayer() {
        if (turnNumber % 2 == 0) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    private boolean canCommand(String username) {
        return (turnNumber % 2 == 0 && username.equalsIgnoreCase(playerTwo.getUserName()))
                || (turnNumber % 2 == 1 && username.equalsIgnoreCase(playerOne.getUserName()));
    }

    public void changeTurn(String username) throws LogicException {
        try {
            if (canCommand(username)) {
                getCurrentTurnPlayer().setCurrentMP(0);

                addNextCardToHand();

                revertNotDurableBuffs();
                removeFinishedBuffs();
                turnNumber++;
                setAllTroopsCanAttackAndCanMove();
                applyAllBuffs();
                if (turnNumber < 14)
                    getCurrentTurnPlayer().increaseMP(turnNumber / 2 + 2);
                else
                    getCurrentTurnPlayer().increaseMP(9);
                Server.getInstance().sendGameUpdateMessage(this);

                startTurnTimeLimit();

                if (getCurrentTurnPlayer().getUserName().equals("AI")) {
                    playCurrentTurn();
                }
            } else {
                throw new ClientException("it isn't your turn!");
            }
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private void startTurnTimeLimit() {
        final int currentTurn = turnNumber;
        new Thread(() -> {
            try {
                Thread.sleep(TURN_TIME_LIMIT);
                if (isFinished) return;
                if (turnNumber == currentTurn) {
                    changeTurn(getCurrentTurnPlayer().getUserName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (LogicException ignored) {
            }
        }).start();
    }

    private void addNextCardToHand() {
        //If you want to draw 2 cards at the end of your turn, set the for loop to run 2 times
        //If you want to draw 1 card at the end of your turn, set the for loop to run 1 time or remove it.
        for(int i = 0; i < 2; i++) {
            Card nextCard = getCurrentTurnPlayer().getNextCard();
            if (getCurrentTurnPlayer().addNextCardToHand()) {
                Server.getInstance().sendChangeCardPositionMessage(this, nextCard, CardPosition.HAND);
                Server.getInstance().sendChangeCardPositionMessage(this, getCurrentTurnPlayer().getNextCard(), CardPosition.NEXT);
            }
        }
    }

    private void playCurrentTurn() throws LogicException {
        try {
            AvailableActions actions = new AvailableActions();
            actions.calculateAvailableActions(this);
            while (actions.getMoves().size() > 0) {
                Move move = actions.getMoves().get(new Random().nextInt(actions.getMoves().size()));
                moveTroop("AI", move.getTroop().getCard().getCardId(), move.getTargets().get(new Random().nextInt(move.getTargets().size())));
                Thread.sleep(500);
                actions.calculateAvailableMoves(this);
            }
            actions.calculateAvailableAttacks(this);
            while (actions.getAttacks().size() > 0) {
                Attack attack = actions.getAttacks().get(new Random().nextInt(actions.getAttacks().size()));
                attack("AI", attack.getAttackerTroop().getCard().getCardId(), attack.getDefenders().get(new Random().nextInt(attack.getDefenders().size())).getCard().getCardId());
                Thread.sleep(500);
                actions.calculateAvailableAttacks(this);
            }
            actions.calculateAvailableInsets(this);
            while (actions.getHandInserts().size() > 0) {
                Insert insert = actions.getHandInserts().get(new Random().nextInt(actions.getHandInserts().size()));
                insert("AI", insert.getCard().getCardId(), new Position(new Random().nextInt(5), new Random().nextInt(9)));
                Thread.sleep(500);
                actions.calculateAvailableInsets(this);
            }
        } catch (InterruptedException ignored) {
        } finally {
            changeTurn("AI");
        }

    }

    private void removeFinishedBuffs() {
        buffs.removeIf(buff -> (buff.getAction().getDuration() == 0));
    }

    private void setAllTroopsCanAttackAndCanMove() {
        for (Troop troop : gameMap.getTroops()) {
            troop.setCanAttack(true);
            troop.setCanMove(true);
            Server.getInstance().sendTroopUpdateMessage(this, troop);
        }
    }

    private void applyAllBuffs() {
        tempBuffs.clear();
        for (Buff buff : buffs) {
            applyBuff(buff);
        }
        buffs.addAll(tempBuffs);
        tempBuffs.clear();
        checkKills();
    }

    private void checkKills() {
        for (Troop troop : gameMap.getTroops()) {
            if (troop.getCurrentHp() <= 0) {
                killTroop(troop);
            }
        }
    }

    private void revertNotDurableBuffs() {
        for (Buff buff : buffs) {
            if (!buff.getAction().isDurable()) {
                revertBuff(buff);
            }
        }
    }

    private void revertBuff(Buff buff) {
        SpellAction action = buff.getAction();

        for (Troop troop : buff.getTarget().getTroops()) {
            if (!(buff.isPositive() || troop.canGiveBadEffect())) continue;

            troop.changeEnemyHit(-action.getEnemyHitChanges());
            troop.changeCurrentAp(-action.getApChange());
            if (!action.isPoison() || troop.canGetPoison()) {
                troop.changeCurrentHp(-action.getHpChange());
            }
            if (action.isMakeStun() && troop.canGetStun()) {
                troop.setCanMove(true);
            }
            if (action.isMakeDisarm() && troop.canGetDisarm()) {
                troop.setDisarm(false);
            }
            if (action.isNoDisarm()) {
                troop.setCantGetDisarm(false);
            }
            if (action.isNoPoison()) {
                troop.setCantGetPoison(false);
            }
            if (action.isNoStun()) {
                troop.setCantGetStun(false);
            }
            if (action.isNoBadEffect()) {
                troop.setDontGiveBadEffect(false);
            }
            if (action.isNoAttackFromWeakerOnes()) {
                troop.setNoAttackFromWeakerOnes(false);
            }
            if (action.isDisableHolyBuff()) {
                troop.setDisableHolyBuff(false);
            }
            Server.getInstance().sendTroopUpdateMessage(this, troop);
        }
    }

    public void insert(String username, String cardId, Position position) throws LogicException {
        try {
            if (!canCommand(username)) {
                throw new ClientException("it's not your turn");
            }

            if (!gameMap.isInMap(position)) {
                throw new ClientException("target cell is not in map");
            }

            Player player = getCurrentTurnPlayer();
            Card card = player.insert(cardId);

            if (card.getType() == CardType.MINION) {
                if (gameMap.getTroop(position) != null) {
                    throw new ClientException("another troop is here.");
                }
                Server.getInstance().sendChangeCardPositionMessage(this, card, CardPosition.MAP);
                Troop troop = new Troop(card, getCurrentTurnPlayer().getPlayerNumber());
                player.addTroop(troop);
                putMinion(
                        player.getPlayerNumber(),
                        troop,
                        gameMap.getCell(position)
                );
                for (Card item : gameMap.getCell(position).getItems()) {
                    if (item.getType() == CardType.FLAG) {
                        catchFlag(troop, item);
                    } else if (item.getType() == CardType.COLLECTIBLE_ITEM) {
                        catchItem(item);
                    }
                }
                Server.getInstance().sendTroopUpdateMessage(this, troop);
                gameMap.getCell(position).clearItems();
            }
            if (card.getType() == CardType.SPELL || card.getType() == CardType.COLLECTIBLE_ITEM) {
                player.addToGraveYard(card);
                Server.getInstance().sendChangeCardPositionMessage(this, card, CardPosition.GRAVE_YARD);
            }
            applyOnPutSpells(card, gameMap.getCell(position));
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private void putMinion(int playerNumber, Troop troop, Cell cell) {
        troop.setCell(cell);
        gameMap.addTroop(playerNumber, troop);
        Server.getInstance().sendTroopUpdateMessage(this, troop);
    }

    private void applyOnPutSpells(Card card, Cell cell) {
        for (Spell spell : card.getSpells()) {
            if (spell.getAvailabilityType().isOnPut()) {
                applySpell(spell, detectTarget(spell, cell, cell, getCurrentTurnPlayer().getHero().getCell()));
            }
        }
    }

    public void moveTroop(String username, String cardId, Position position) throws LogicException {
        if (!canCommand(username)) {
            throw new ClientException("its not your turn");
        }

        if (!gameMap.isInMap(position)) {
            throw new ClientException("coordination is not valid");
        }

        Troop troop = gameMap.getTroop(cardId);
        if (troop == null) {
            throw new ClientException("select a valid card");
        }
        if (troop.getCell().manhattanDistance(position) > 2) {
            throw new ClientException("too far to go");
        }
        if (!troop.canMove()) {
            throw new ClientException("troop can not move");
        }

        Cell cell = gameMap.getCell(position);
        troop.setCell(cell);
        troop.setCanMove(false);

        for (Card item : cell.getItems()) {
            if (item.getType() == CardType.FLAG) {
                catchFlag(troop, item);
            } else if (item.getType() == CardType.COLLECTIBLE_ITEM) {
                catchItem(item);
            }
        }
        Server.getInstance().sendTroopUpdateMessage(this, troop);
        cell.clearItems();
    }

    void catchFlag(Troop troop, Card item) throws ServerException {
        troop.addFlag(item);
        getCurrentTurnPlayer().increaseNumberOfCollectedFlags();
        getCurrentTurnPlayer().addFlagCarrier(troop);
        Server.getInstance().sendGameUpdateMessage(this);
    }

    private void catchItem(Card item) {
        getCurrentTurnPlayer().collectItem(item);
        Server.getInstance().sendChangeCardPositionMessage(this, item, CardPosition.COLLECTED);
    }

    public void attack(String username, String attackerCardId, String defenderCardId) throws LogicException {
        try {
            if (!canCommand(username)) {
                throw new ClientException("its not your turn");
            }

            Troop attackerTroop = getAndValidateTroop(attackerCardId, getCurrentTurnPlayer());
            Troop defenderTroop = getAndValidateTroop(defenderCardId, getOtherTurnPlayer());

            if (!attackerTroop.canAttack()) {
                throw new ClientException("attacker can not attack");
            }

            checkRangeForAttack(attackerTroop, defenderTroop);

            if (defenderTroop.canGiveBadEffect() &&
                    (defenderTroop.canBeAttackedFromWeakerOnes() || attackerTroop.getCurrentAp() > defenderTroop.getCurrentAp())
            ) {
                damage(attackerTroop, defenderTroop);

                attackerTroop.setCanAttack(false);
                attackerTroop.setCanMove(false);
                Server.getInstance().sendTroopUpdateMessage(this, attackerTroop);
                applyOnAttackSpells(attackerTroop, defenderTroop);
                applyOnDefendSpells(defenderTroop, attackerTroop);
                try {
                    counterAttack(defenderTroop, attackerTroop);
                } catch (LogicException e) {
                    Server.getInstance().sendAttackMessage(this, attackerTroop, defenderTroop, false);
                    throw e;
                }
                Server.getInstance().sendAttackMessage(this, attackerTroop, defenderTroop, true);
            }
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private void applyOnAttackSpells(Troop attackerTroop, Troop defenderTroop) {
        for (Spell spell : attackerTroop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnAttack())
                applySpell(
                        spell,
                        detectTarget(spell, defenderTroop.getCell(), defenderTroop.getCell(), getCurrentTurnPlayer().getHero().getCell())
                );
        }
    }

    private void applyOnDefendSpells(Troop defenderTroop, Troop attackerTroop) {
        for (Spell spell : defenderTroop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnDefend())
                applySpell(
                        spell,
                        detectTarget(spell, attackerTroop.getCell(), attackerTroop.getCell(), getOtherTurnPlayer().getHero().getCell())
                );
        }
    }

    private void counterAttack(Troop defenderTroop, Troop attackerTroop) throws LogicException {
        if (defenderTroop.isDisarm()) {
            throw new ClientException("defender is disarm");
        }

        checkRangeForAttack(defenderTroop, attackerTroop);

        if (attackerTroop.canGiveBadEffect() &&
                (attackerTroop.canBeAttackedFromWeakerOnes() || defenderTroop.getCurrentAp() > attackerTroop.getCurrentAp())
        ) {
            damage(defenderTroop, attackerTroop);
        }
    }

    private void damage(Troop attackerTroop, Troop defenderTroop) {
        int attackPower = calculateAp(attackerTroop, defenderTroop);

        defenderTroop.changeCurrentHp(-attackPower);

        if (defenderTroop.getCurrentHp() <= 0) {
            killTroop(defenderTroop);
        } else {
            Server.getInstance().sendTroopUpdateMessage(this, defenderTroop);
        }
    }

    private int calculateAp(Troop attackerTroop, Troop defenderTroop) {
        int attackPower = attackerTroop.getCurrentAp();
        if (!attackerTroop.isHolyBuffDisabling() || defenderTroop.getEnemyHitChanges() > 0) {
            attackPower += defenderTroop.getEnemyHitChanges();
        }
        return attackPower;
    }

    public void useSpecialPower(String username, String cardId, Position target) throws LogicException {
        try {
            if (!canCommand(username)) {
                throw new ClientException("its not your turn");
            }

            Troop hero = getAndValidateHero(cardId);
            Spell specialPower = getAndValidateSpecialPower(hero);
            getCurrentTurnPlayer().changeCurrentMP(-specialPower.getMannaPoint());

            applySpell(
                    specialPower,
                    detectTarget(specialPower, hero.getCell(), gameMap.getCell(target), hero.getCell())
            );
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private Troop getAndValidateHero(String cardId) throws ClientException {
        Troop hero = getCurrentTurnPlayer().getHero();
        if (hero == null || !hero.getCard().getCardId().equalsIgnoreCase(cardId)) {
            throw new ClientException("hero id is not valid");
        }
        return hero;
    }

    private Spell getAndValidateSpecialPower(Troop hero) throws ClientException {
        Spell specialPower = hero.getCard().getSpells().get(0);
        if (specialPower == null || !specialPower.getAvailabilityType().isSpecialPower()) {
            throw new ClientException("special power is not available");
        }

        if (specialPower.isCoolDown(turnNumber)) {
            throw new ClientException("special power is cool down");
        }

        if (getCurrentTurnPlayer().getCurrentMP() < specialPower.getMannaPoint()) {
            throw new ClientException("insufficient manna");
        }
        return specialPower;
    }

    private Troop getAndValidateTroop(String defenderCardId, Player otherTurnPlayer) throws ClientException {
        Troop troop = otherTurnPlayer.getTroop(defenderCardId);
        if (troop == null) {
            throw new ClientException("card id is not valid");
        }
        return troop;
    }

    private Troop[] getAndValidateAttackerTroops(String[] attackerCardIds, Troop defenderTroop) throws ClientException {
        Troop[] attackerTroops = new Troop[attackerCardIds.length];
        for (int i = 0; i < attackerTroops.length; i++) {
            attackerTroops[i] = getCurrentTurnPlayer().getTroop(attackerCardIds[i]);
            if (attackerTroops[i] == null) {
                throw new ClientException("invalid attacker troop");
            }

            checkRangeForAttack(attackerTroops[i], defenderTroop);
        }
        return attackerTroops;
    }

    private void checkRangeForAttack(Troop attackerTroop, Troop defenderTroop) throws ClientException {
        if (attackerTroop.getCard().getAttackType() == AttackType.MELEE) {
            if (!attackerTroop.getCell().isNextTo(defenderTroop.getCell())) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        } else if (attackerTroop.getCard().getAttackType() == AttackType.RANGED) {
            if (attackerTroop.getCell().isNextTo(defenderTroop.getCell()) ||
                    attackerTroop.getCell().manhattanDistance(defenderTroop.getCell()) > attackerTroop.getCard().getRange()) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        } else { // HYBRID
            if (attackerTroop.getCell().manhattanDistance(defenderTroop.getCell()) > attackerTroop.getCard().getRange()) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        }
    }

    private void damageFromAllAttackers(Troop defenderTroop, Troop[] attackerTroops) {
        for (Troop attackerTroop : attackerTroops) {
            if (defenderTroop.canGiveBadEffect() &&
                    (defenderTroop.canBeAttackedFromWeakerOnes() || attackerTroop.getCurrentAp() > defenderTroop.getCurrentAp())
            ) {
                damage(attackerTroop, defenderTroop);

                attackerTroop.setCanAttack(false);
                attackerTroop.setCanMove(false);
                Server.getInstance().sendTroopUpdateMessage(this, attackerTroop);

                applyOnAttackSpells(attackerTroop, defenderTroop);
            }
        }
    }

    public abstract boolean finishCheck();

    void finish() {
        isFinished = true;
    }

    private void applySpell(Spell spell, TargetData target) {
        spell.setLastTurnUsed(turnNumber);
        Buff buff = new Buff(spell.getAction(), target);
        Server.getInstance().sendSpellMessage(this, target, spell.getAvailabilityType());
        buffs.add(buff);
        applyBuff(buff);
    }

    private void applyBuff(Buff buff) {
        TargetData target = buff.getTarget();
        if (haveDelay(buff)) return;

        applyBuffOnCards(buff, target.getCards());
        applyBuffOnCellTroops(buff, target.getCells());
        applyBuffOnTroops(buff, target.getTroops());
        applyBuffOnPlayers(buff, target.getPlayers());

        decreaseDuration(buff);
    }

    private void applyBuffOnPlayers(Buff buff, List<Player> players) {
        SpellAction action = buff.getAction();
        for (Player player : players) {
            player.changeCurrentMP(action.getMpChange());
            Server.getInstance().sendGameUpdateMessage(this);
        }
    }

    private void decreaseDuration(Buff buff) {
        SpellAction action = buff.getAction();
        if (action.getDuration() > 0) {
            action.decreaseDuration();
        }
    }

    private boolean haveDelay(Buff buff) {
        SpellAction action = buff.getAction();
        if (action.getDelay() > 0) {
            action.decreaseDelay();
            return true;
        }
        return false;
    }

    private void applyBuffOnCards(Buff buff, List<Card> cards) {
        SpellAction action = buff.getAction();
        for (Card card : cards) {
            if (action.isAddSpell()) {
                card.addSpell(action.getCarryingSpell());
            }
        }
    }

    private void applyBuffOnCellTroops(Buff buff, List<Cell> cells) {
        ArrayList<Troop> inCellTroops = getInCellTargetTroops(cells);
        Buff troopBuff = new Buff(
                buff.getAction().makeCopyAction(1, 0), new TargetData(inCellTroops)
        );
        tempBuffs.add(troopBuff);
        applyBuffOnTroops(troopBuff, inCellTroops);
    }

    private void applyBuffOnTroops(Buff buff, List<Troop> targetTroops) {
        SpellAction action = buff.getAction();
        for (Troop troop : targetTroops) {
            if (!(buff.isPositive() || troop.canGiveBadEffect())) continue;

            troop.changeEnemyHit(action.getEnemyHitChanges());
            troop.changeCurrentAp(action.getApChange());
            if (!action.isPoison() || troop.canGetPoison()) {
                troop.changeCurrentHp(action.getHpChange());
                if (troop.getCurrentHp() <= 0) {
                    killTroop(troop);
                }
            }
            if (action.isMakeStun() && troop.canGetStun()) {
                troop.setCanMove(false);
            }
            if (action.isMakeDisarm() && troop.canGetDisarm()) {
                troop.setDisarm(true);
            }
            if (action.isNoDisarm()) {
                troop.setCantGetDisarm(true);
            }
            if (action.isNoPoison()) {
                troop.setCantGetPoison(true);
            }
            if (action.isNoStun()) {
                troop.setCantGetStun(true);
            }
            if (action.isNoBadEffect()) {
                troop.setDontGiveBadEffect(true);
            }
            if (action.isNoAttackFromWeakerOnes()) {
                troop.setNoAttackFromWeakerOnes(true);
            }
            if (action.isDisableHolyBuff()) {
                troop.setDisableHolyBuff(true);
            }
            if (action.isKillsTarget()) {
                killTroop(troop);
            }
            if (action.getRemoveBuffs() > 0) {
                removePositiveBuffs(troop);
            }
            if (action.getRemoveBuffs() < 0) {
                removeNegativeBuffs(troop);
            }

            Server.getInstance().sendTroopUpdateMessage(this, troop);
        }
    }

    private void removePositiveBuffs(Troop troop) {
        for (Buff buff : buffs) {
            if (buff.isPositive() && buff.getAction().getDuration() >= 0) {
                buff.getTarget().getTroops().remove(troop);
            }
        }
    }

    private void removeNegativeBuffs(Troop troop) {
        for (Buff buff : buffs) {
            if (!buff.isPositive() && buff.getAction().getDuration() >= 0) {
                buff.getTarget().getTroops().remove(troop);
            }
        }
    }

    void killTroop(Troop troop) {
        applyOnDeathSpells(troop);
        if (troop.getPlayerNumber() == 1) {
            playerOne.killTroop(this, troop);
            gameMap.removeTroop(playerOne, troop);
        } else if (troop.getPlayerNumber() == 2) {
            playerTwo.killTroop(this, troop);
            gameMap.removeTroop(playerTwo, troop);
        }
        Server.getInstance().sendChangeCardPositionMessage(this, troop.getCard(), CardPosition.GRAVE_YARD);
    }

    private void applyOnDeathSpells(Troop troop) {
        for (Spell spell : troop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnDefend())
                applySpell(
                        spell,
                        detectTarget(spell, troop.getCell(), gameMap.getCell(0, 0), getOtherTurnPlayer().getHero().getCell())
                );
        }
    }

    private ArrayList<Troop> getInCellTargetTroops(List<Cell> cells) {
        ArrayList<Troop> inCellTroops = new ArrayList<>();
        for (Cell cell : cells) {
            Troop troop = playerOne.getTroop(cell);
            if (troop == null) {
                troop = playerTwo.getTroop(cell);
            }
            if (troop != null) {
                inCellTroops.add(troop);
            }
        }
        return inCellTroops;
    }

    private TargetData detectTarget(Spell spell, Cell cardCell, Cell clickCell, Cell heroCell) {
        TargetData targetData = new TargetData();
        Player player;
        if (spell.getTarget().getOwner() != null) {
            if (spell.getTarget().getOwner().isOwn()) {
                player = getCurrentTurnPlayer();
                setTargetData(spell, cardCell, clickCell, heroCell, player, targetData);
            }
            if (spell.getTarget().getOwner().isEnemy()) {
                player = getOtherTurnPlayer();
                setTargetData(spell, cardCell, clickCell, heroCell, player, targetData);
            }
        } else {
            setTargetData(spell, cardCell, clickCell, heroCell, null, targetData);
        }
        if (spell.getTarget().isRandom()) {
            randomizeList(targetData.getTroops());
            randomizeList(targetData.getCells());
            randomizeList(targetData.getPlayers());
            randomizeList(targetData.getCards());
        }
        return targetData;
    }

    private void setTargetData(Spell spell, Cell cardCell, Cell clickCell, Cell heroCell, Player player, TargetData targetData) {

        if (spell.getTarget().getCardType().isPlayer()) {
            targetData.getPlayers().add(player);
        }

        if (spell.getTarget().isForDeckCards()) {
            for (Card card : player.getDeck().getOthers()) {
                addCardToTargetData(spell, targetData, card);
            }
            for (Card card : player.getHand()) {
                addCardToTargetData(spell, targetData, card);
            }
            addCardToTargetData(spell, targetData, player.getNextCard());
            addCardToTargetData(spell, targetData, player.getDeck().getHero());
        }
        if (spell.getTarget().getDimensions() != null) {
            Position centerPosition = getCenterPosition(spell, cardCell, clickCell, heroCell);
            ArrayList<Cell> targetCells = detectCells(centerPosition, spell.getTarget().getDimensions());
            addTroopsAndCellsToTargetData(spell, targetData, player, targetCells);
        }

    }

    private Position getCenterPosition(Spell spell, Cell cardCell, Cell clickCell, Cell heroCell) {
        Position centerPosition;
        if (spell.getTarget().isRelatedToCardOwnerPosition()) {
            centerPosition = new Position(cardCell);
        } else if (spell.getTarget().isForAroundOwnHero()) {
            centerPosition = new Position(heroCell);
        } else {
            centerPosition = new Position(clickCell);
        }
        return centerPosition;
    }

    private <T> void randomizeList(List<T> list) {
        if (list.size() == 0) return;

        int random = new Random().nextInt(list.size());
        T e = list.get(random);
        list.clear();
        list.add(e);
    }

    private void addCardToTargetData(Spell spell, TargetData targetData, Card card) {
        if (spell.getTarget().getCardType().isHero() && card.getType() == CardType.HERO)
            targetData.getCards().add(card);
        if (spell.getTarget().getCardType().isMinion() && card.getType() == CardType.MINION)
            targetData.getCards().add(card);
    }

    private void addTroopsAndCellsToTargetData(Spell spell, TargetData targetData, Player player, ArrayList<Cell> targetCells) {
        for (Cell cell : targetCells) {
            if (player != null) {
                Troop troop = player.getTroop(cell);
                if (troop != null) {
                    if (spell.getTarget().getAttackType().isHybrid() && troop.getCard().getAttackType() == AttackType.HYBRID) {
                        addTroopToTargetData(spell, targetData, troop);
                    }
                    if (spell.getTarget().getAttackType().isMelee() && troop.getCard().getAttackType() == AttackType.MELEE) {
                        addTroopToTargetData(spell, targetData, troop);
                    }
                    if (spell.getTarget().getAttackType().isRanged() && troop.getCard().getAttackType() == AttackType.RANGED) {
                        addTroopToTargetData(spell, targetData, troop);
                    }
                }
            }
            if (spell.getTarget().getCardType().isCell()) {
                targetData.getCells().add(cell);
            }
        }
    }

    private void addTroopToTargetData(Spell spell, TargetData targetData, Troop troop) {
        if (spell.getTarget().getCardType().isHero() && troop.getCard().getType() == CardType.HERO) {
            targetData.getTroops().add(troop);
        }
        if (spell.getTarget().getCardType().isMinion() && troop.getCard().getType() == CardType.MINION) {
            targetData.getTroops().add(troop);
        }
    }

    private ArrayList<Cell> detectCells(Position centerPosition, Position dimensions) {
        int firstRow = calculateFirstCoordinate(centerPosition.getRow(), dimensions.getRow());
        int firstColumn = calculateFirstCoordinate(centerPosition.getColumn(), dimensions.getColumn());

        int lastRow = calculateLastCoordinate(firstRow, dimensions.getRow(), GameMap.getRowNumber());
        int lastColumn = calculateLastCoordinate(firstColumn, dimensions.getColumn(), GameMap.getColumnNumber());
        ArrayList<Cell> targetCells = new ArrayList<>();
        for (int i = firstRow; i < lastRow; i++) {
            for (int j = firstColumn; j < lastColumn; j++) {
                if (gameMap.isInMap(i, j))
                    targetCells.add(gameMap.getCells()[i][j]);
            }
        }
        return targetCells;
    }

    private int calculateFirstCoordinate(int center, int dimension) {
        int firstCoordinate = center - (dimension - 1) / 2;
        if (firstCoordinate < 0)
            firstCoordinate = 0;
        return firstCoordinate;
    }

    private int calculateLastCoordinate(int first, int dimension, int maxNumber) {
        int lastRow = first + dimension;
        if (lastRow > maxNumber) {
            lastRow = maxNumber;
        }
        return lastRow;
    }

    void setMatchHistories(boolean resultOne, boolean resultTwo) {
        playerOne.setMatchHistory(
                new MatchHistory(playerTwo, resultOne)
        );

        playerTwo.setMatchHistory(
                new MatchHistory(playerOne, resultTwo)
        );
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public List<CellEffect> getCellEffects() {
        List<CellEffect> result = new ArrayList<>();

        buffs.forEach(buff -> buff.getTarget().getCells()
                .forEach(cell -> result.add(new CellEffect(new Position(cell), buff.isPositive())))
        );
        return Collections.unmodifiableList(result);
    }

    public void forceFinish(String username) {
        setMatchHistories(!playerOne.getUserName().equals(username), !playerTwo.getUserName().equals(username));
        finish();
    }

    public GameType getGameType() {
        return gameType;
    }

    public void addObserver(Account account) {
        observers.add(account);
    }

    public void removeObserver(Account account) {
        observers.remove(account);
    }

    public ArrayList<Account> getObservers() {
        return observers;
    }
}