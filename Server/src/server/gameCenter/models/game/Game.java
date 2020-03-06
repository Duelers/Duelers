package server.gameCenter.models.game;

import server.GameServer;
import server.clientPortal.models.comperessedData.CompressedGame;
import server.clientPortal.models.message.CardPosition;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.MatchHistory;

import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;

import server.dataCenter.models.card.Deck;

import shared.models.card.spell.Spell;
import shared.models.card.spell.SpellAction;

import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.gameCenter.GameCenter;
import server.gameCenter.models.game.availableActions.Attack;
import server.gameCenter.models.game.availableActions.AvailableActions;
import server.gameCenter.models.game.availableActions.Insert;
import server.gameCenter.models.game.availableActions.Move;

import shared.models.game.GameType;
import shared.models.game.map.Cell;

import server.gameCenter.models.map.GameMap;

import shared.models.game.map.CellEffect;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Game {
    private static final long TURN_TIME_LIMIT = 120000;
    private final Player playerOne;
    private final Player playerTwo;
    private final GameType gameType;
    private final ArrayList<Buff> buffs = new ArrayList<>();
    private final ArrayList<Buff> tempBuffs = new ArrayList<>();
    private final GameMap gameMap;
    private int turnNumber = 1;
    private boolean isFinished;
    private final ArrayList<Account> observers = new ArrayList<>();

    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private Runnable task;
    private ScheduledFuture<?> future;

    protected Game(Account account, Deck secondDeck, String userName, GameMap gameMap, GameType gameType) {
        this.gameType = gameType;
        this.gameMap = gameMap;
        this.playerOne = new Player(account.getMainDeck(), account.getUsername(), 1);
        this.playerTwo = new Player(secondDeck, userName, 2);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void startGame() {
        playerOne.setCurrentMP(2);

        putMinion(1, playerOne.createHero(), gameMap.getCell(2, 0));

        this.turnNumber = 2;
        putMinion(2, playerTwo.createHero(), gameMap.getCell(2, 8));

        this.turnNumber = 1;

        startTurnTimeLimit();

        GameServer.getInstance().sendGameUpdateMessage(this);
    }

    public CompressedGame toCompressedGame() {
        return new CompressedGame(playerOne, playerTwo, gameMap, turnNumber, gameType);
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

    public void changeTurn(String username, boolean forced) throws LogicException {
        try {
            if (!forced)
                this.cancelTimeLimit();
            if (canCommand(username)) {
                getCurrentTurnPlayer().setCurrentMP(0);

                addNextCardToHand();
                getCurrentTurnPlayer().setNumTimesReplacedThisTurn(0);

                revertNotDurableBuffs();
                removeFinishedBuffs();
                turnNumber++;
                setAllTroopsCanAttackAndCanMove();
                applyAllBuffs();
                if (turnNumber < 14)
                    getCurrentTurnPlayer().increaseMP(turnNumber / 2 + 2);
                else
                    getCurrentTurnPlayer().increaseMP(9);
                GameServer.getInstance().sendGameUpdateMessage(this);

                startTurnTimeLimit();

                if (getCurrentTurnPlayer().getUserName().equals("AI")) {
                    playCurrentTurnAtRandom();
                }
            } else {
                throw new ClientException("it isn't your turn!");
            }
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    void cancelTimeLimit() {
        this.future.cancel(true);
    }

    private void startTurnTimeLimit() {
        final int currentTurn = turnNumber;

        this.task = () -> {
            try {
                if (this.turnNumber == currentTurn)
                    changeTurn(getCurrentTurnPlayer().getUserName(), true);
            } catch (LogicException ignored) {}
        };
        this.future = this.timer.schedule(this.task, 120, TimeUnit.SECONDS);
    }

    private void addNextCardToHand() {
        //If you want to draw 2 cards at the end of your turn, set the for loop to run 2 times
        //If you want to draw 1 card at the end of your turn, set the for loop to run 1 time or remove it.
        for (int i = 0; i < 2; i++) {
            Card nextCard = getCurrentTurnPlayer().getNextCard();
            if (getCurrentTurnPlayer().addNextCardToHand()) {
                GameServer.getInstance().sendChangeCardPositionMessage(this, nextCard, CardPosition.HAND);
                GameServer.getInstance().sendChangeCardPositionMessage(this, getCurrentTurnPlayer().getNextCard(), CardPosition.NEXT);
            }
        }
    }

    public void setNewNextCard() {
        getCurrentTurnPlayer().setNewNextCard();
        GameServer.getInstance().sendNewNextCardSetMessage(this, getCurrentTurnPlayer().getNextCard());
    }

    public void replaceCard(String cardID) throws LogicException {
        if (getCurrentTurnPlayer().getCanReplaceCard()) {
            Card removedCard = getCurrentTurnPlayer().removeCardFromHand(cardID);
            if (removedCard == null) {
                return;
            }
            getCurrentTurnPlayer().addCardToDeck(removedCard);
            if (getCurrentTurnPlayer().addNextCardToHand()) {
                Card nextCard = getCurrentTurnPlayer().getNextCard();
                int numTimesReplacedThisTurn = getCurrentTurnPlayer().getNumTimesReplacedThisTurn();
                getCurrentTurnPlayer().setNumTimesReplacedThisTurn(numTimesReplacedThisTurn + 1);
                GameServer.getInstance().sendChangeCardPositionMessage(this, removedCard, CardPosition.MAP);
                GameServer.getInstance().sendChangeCardPositionMessage(this, nextCard, CardPosition.HAND);
                GameServer.getInstance().sendChangeCardPositionMessage(this, nextCard, CardPosition.NEXT);
            }
        } else {
            System.out.println("Cannot replace card. Current canReplaceCard value: " + getCurrentTurnPlayer().getCanReplaceCard());
        }
    }

    private void playCurrentTurnAtRandom() throws LogicException {
        // AI
        final int delay = 1000;
        try {
            AvailableActions actions = new AvailableActions();
            actions.calculateAvailableActions(this);
            while (actions.getMoves().size() > 0) {
                Move move = actions.getMoves().get(new Random().nextInt(actions.getMoves().size()));
                moveTroop("AI", move.getTroop().getCard().getCardId(), move.getTargets().get(new Random().nextInt(move.getTargets().size())));
                Thread.sleep(delay);
                actions.calculateAvailableMoves(this);
            }
            actions.calculateAvailableAttacks(this);
            while (actions.getAttacks().size() > 0) {
                Attack attack = actions.getAttacks().get(new Random().nextInt(actions.getAttacks().size()));
                attack("AI", attack.getAttackerTroop().getCard().getCardId(), attack.getDefenders().get(new Random().nextInt(attack.getDefenders().size())).getCard().getCardId());
                Thread.sleep(delay);
                actions.calculateAvailableAttacks(this);
            }
            actions.calculateAvailableInserts(this);
            while (actions.getHandInserts().size() > 0) {

                int currentMana = getCurrentTurnPlayer().getCurrentMP();

                // Pick a playable minion in the hand at random.
                // By "playable" we simply check available mana relative to minion cost.
                ArrayList<Card> minionOptions = new ArrayList<Card>();
                for (Insert i : actions.getHandInserts()) {
                    if (i.getCard().getManaCost() <= currentMana && i.getCard().getType() == CardType.MINION) {
                        minionOptions.add(i.getCard());
                    }
                }

                if (minionOptions.isEmpty()) {
                    break;
                }

                System.out.print("AI PLAYER: minion(s) in Hand = ");
                minionOptions.forEach((n) -> System.out.print(n.getName() + ", "));
                System.out.print("\n");

                int idx = new Random().nextInt(Math.max(1, minionOptions.size() - 1));
                Card minion = minionOptions.get(idx);

                // Skew probability distribution towards favoring squares closer to Hero position.
                int[] offsets = new int[]{-3, -2, -2, -1, -1, -1, 0, 0, 0, 1, 1, 1, 2, 2, 3};
                Cell HeroPosition = getCurrentTurnPlayer().getHero().getCell();

                // Attempt (max n tries) to place minion on a random square.
                for (int attempts = 0; attempts < 20; attempts++) {

                    int x = offsets[new Random().nextInt(offsets.length)];
                    int y = offsets[new Random().nextInt(offsets.length)];

                    // Get a random square, force it to be within index bounds.
                    int x2 = Math.max(0, Math.min(x + HeroPosition.getRow(), gameMap.getNumRows() - 1));
                    int y2 = Math.max(0, Math.min(y + HeroPosition.getColumn(), gameMap.getNumColumns() - 1));

                    Cell c = new Cell(x2, y2);

                    if (isLegalCellForMinion(c, minion)) {
                        insert("AI", minion.getCardId(), new Cell(c.getRow(), c.getColumn()));
                        Thread.sleep(delay);
                        break;
                    }
                }
                actions.calculateAvailableInserts(this);
            }
        } catch (InterruptedException ignored) {
            ignored.printStackTrace();
        } finally {
            changeTurn("AI", false);
        }

    }

    private void removeFinishedBuffs() {
        buffs.removeIf(buff -> (buff.getAction().getDuration() == 0));
    }

    private void setAllTroopsCanAttackAndCanMove() {
        for (ServerTroop troop : gameMap.getTroops()) {
            troop.resetRemainingMovesAndAttacks();
            troop.setCanAttack(true);
            troop.setCanMove(true);
            GameServer.getInstance().sendTroopUpdateMessage(this, troop);
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
        for (ServerTroop troop : gameMap.getTroops()) {
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

        for (ServerTroop troop : buff.getTarget().getTroops()) {
            if (!(buff.isPositive() || troop.canGiveBadEffect())) continue;

            troop.changeEnemyHit(-action.getEnemyHitChanges());
            troop.changeCurrentAp(-action.getApChange());
            if (!action.isPoison() || troop.canGetPoison()) {
                troop.changeCurrentHp(-action.getHpChange());
            }
            if (action.isMakeStun() && troop.canGetStun()) {
                troop.setCanMove(true);
                troop.setCanAttack(true);
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
            GameServer.getInstance().sendTroopUpdateMessage(this, troop);
        }
    }

    public void insert(String username, String cardId, Cell cell) throws LogicException {
        try {
            if (!canCommand(username)) {
                throw new ClientException("it's not your turn");
            }

            if (!gameMap.isInMap(cell)) {
                throw new ClientException(cell.toString() + " is not in map");
            }

            Player player = getCurrentTurnPlayer();
            Card card = player.insert(cardId);

            if (card.getType() == CardType.MINION) {
                if (gameMap.getTroop(cell) != null) {
                    throw new ClientException("another troop is here.");
                }
                GameServer.getInstance().sendChangeCardPositionMessage(this, card, CardPosition.MAP);
                ServerTroop troop = new ServerTroop(card, getCurrentTurnPlayer().getPlayerNumber());

                if (troop.getCard().getDescription().contains("Rush")) {
                    troop.setCanAttack(true);
                    troop.setCanMove(true);
                }
                else{
                    troop.setCanAttack(false);
                    troop.setCanMove(false);
                }

                player.addTroop(troop);
                putMinion(
                        player.getPlayerNumber(),
                        troop,
                        gameMap.getCell(cell)
                );

                GameServer.getInstance().sendTroopUpdateMessage(this, troop);
            }
            if (card.getType() == CardType.SPELL) {
                player.addToGraveYard(card);
                GameServer.getInstance().sendChangeCardPositionMessage(this, card, CardPosition.GRAVE_YARD);
            }
            applyOnPutSpells(card, gameMap.getCell(cell));
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private void putMinion(int playerNumber, ServerTroop troop, Cell cell) {

        if (!(troop.getCard().getType() == CardType.HERO)) {
            // This function is also used to place heroes at start of game, hence this check.
            if (!isLegalCellForMinion(cell, troop.getCard())) {
                // Note: there is a bug where is you target an illegal square the game gets in an unplayable state
                return;
            }
        }

        troop.setCell(cell);
        gameMap.addTroop(playerNumber, troop);
        GameServer.getInstance().sendTroopUpdateMessage(this, troop);
    }

    private boolean isLegalCellForMinion(Cell cell, Card card) {

        if (!(gameMap.getTroop(cell) == null)) {
            // square is not empty
            return false;
        }

        // Minion Placement rules: nearby ally <Hero, Minion>
        // Note, hard-coded the AIRDROP keyword ability
        if (card.getDescription().contains("Airdrop")) {
            System.out.println(cell.toString() + " Is a legal square because " + card.getCardId() + " has AIRDROP keyword.");
            return true;
        }

        Player player = getCurrentTurnPlayer();

        for (ServerTroop troop : player.getTroops()) {
            Cell allyPosition = troop.getCell();

            boolean checkRow = Math.abs(cell.getRow() - allyPosition.getRow()) <= 1;
            boolean checkColumn = Math.abs(cell.getColumn() - allyPosition.getColumn()) <= 1;

            if (checkRow && checkColumn) {
                System.out.println(cell.toString() + " Is a legal square because Ally UNIT: " + troop.getCard().getCardId()
                        + " Is on " + allyPosition.toString());
                return true;
            }
        }
        return false;
    }

    private void applyOnPutSpells(Card card, Cell cell) {
        for (Spell spell : card.getSpells()) {
            if (spell.getAvailabilityType().isOnPut()) {
                applySpell(spell, detectTarget(spell, cell, cell, getCurrentTurnPlayer().getHero().getCell()));
            }
        }
    }

    public void moveTroop(String username, String cardId, Cell cell) throws LogicException {
        if (!canCommand(username)) {
            throw new ClientException("its not your turn");
        }

        if (!gameMap.isInMap(cell)) {
            throw new ClientException("given coordinate is not valid");
        }

        ServerTroop troop = gameMap.getTroop(cardId);
        if (troop == null) {
            throw new ClientException("select a valid card");
        }

        if (!troop.canMove()) {
            throw new ClientException("troop can not move");
        }

        // TODO: Check if position is under provoke of enemy minion. If yes, raise exception

        if (!troop.getCard().getDescription().contains("Flying")) {
            if (troop.getCell().manhattanDistance(cell) > 2) {
                throw new ClientException("too far to go");
            }
        }

        Cell newCell = gameMap.getCell(cell);
        troop.setCell(newCell);

        if (troop.getRemainingMoves() < troop.getRemainingAttacks()) {
            troop.reduceRemainingAttacks();
        }

        troop.reduceRemainingMoves();
        if (troop.noMovesRemaining()) {
            troop.setCanMove(false);
        }

        GameServer.getInstance().sendTroopUpdateMessage(this, troop);
    }

    public void attack(String username, String attackerCardId, String defenderCardId) throws LogicException {
        try {
            if (!canCommand(username)) {
                throw new ClientException("its not your turn");
            }

            ServerTroop attackerTroop = getAndValidateTroop(attackerCardId, getCurrentTurnPlayer());
            ServerTroop defenderTroop = getAndValidateTroop(defenderCardId, getOtherTurnPlayer());

            if (!attackerTroop.canAttack()) {
                throw new ClientException("attacker can not attack");
            }

            checkRangeForAttack(attackerTroop, defenderTroop);

            if (defenderTroop.canGiveBadEffect() &&
                    (defenderTroop.canBeAttackedFromWeakerOnes() || attackerTroop.getCurrentAp() > defenderTroop.getCurrentAp())
            ) {
                attackerTroop.reduceRemainingAttacks();
                if (attackerTroop.noAttacksRemaining()) {
                    attackerTroop.setCanAttack(false);
                    attackerTroop.setCanMove(false);
                }

                if (attackerTroop.getRemainingAttacks() < attackerTroop.getRemainingMoves()) {
                    attackerTroop.reduceRemainingMoves();
                }

                GameServer.getInstance().sendTroopUpdateMessage(this, attackerTroop);
                applyOnAttackSpells(attackerTroop, defenderTroop);
                applyOnDefendSpells(defenderTroop, attackerTroop);
                try {
                    counterAttack(defenderTroop, attackerTroop);
                } catch (LogicException e) {
                    GameServer.getInstance().sendAttackMessage(this, attackerTroop, defenderTroop, false);
//                    throw e;
                }
                GameServer.getInstance().sendAttackMessage(this, attackerTroop, defenderTroop, true);

                damage(attackerTroop, defenderTroop);
            }
        } finally {
            GameCenter.getInstance().checkGameFinish(this);
        }
    }

    private void applyOnAttackSpells(ServerTroop attackerTroop, ServerTroop defenderTroop) {
        for (Spell spell : attackerTroop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnAttack())
                applySpell(
                        spell,
                        detectTarget(spell, attackerTroop.getCell(), defenderTroop.getCell(), getCurrentTurnPlayer().getHero().getCell())
                );
        }
    }

    private void applyOnDefendSpells(ServerTroop defenderTroop, ServerTroop attackerTroop) {
        for (Spell spell : defenderTroop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnDefend())
                applySpell(
                        spell,
                        detectTarget(spell, attackerTroop.getCell(), attackerTroop.getCell(), getOtherTurnPlayer().getHero().getCell())
                );
        }
    }

    private void counterAttack(ServerTroop defenderTroop, ServerTroop attackerTroop) throws LogicException {
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

    private void damage(ServerTroop attackerTroop, ServerTroop defenderTroop) {
        int attackPower = calculateAp(attackerTroop, defenderTroop);

        defenderTroop.changeCurrentHp(-attackPower);

        if (defenderTroop.getCurrentHp() <= 0) {
            killTroop(defenderTroop);
        } else {
            GameServer.getInstance().sendTroopUpdateMessage(this, defenderTroop);
        }
    }

    private int calculateAp(ServerTroop attackerTroop, ServerTroop defenderTroop) {
        int attackPower = attackerTroop.getCurrentAp();
        if (!attackerTroop.isHolyBuffDisabling() || defenderTroop.getEnemyHitChanges() > 0) {
            attackPower += defenderTroop.getEnemyHitChanges();
        }
        return attackPower;
    }


    private ServerTroop getAndValidateHero(String cardId) throws ClientException {
        ServerTroop hero = getCurrentTurnPlayer().getHero();
        if (hero == null || !hero.getCard().getCardId().equalsIgnoreCase(cardId)) {
            throw new ClientException("hero id is not valid");
        }
        return hero;
    }

    private ServerTroop getAndValidateTroop(String defenderCardId, Player otherTurnPlayer) throws ClientException {
        ServerTroop troop = otherTurnPlayer.getTroop(defenderCardId);
        if (troop == null) {
            throw new ClientException("card id is not valid");
        }
        return troop;
    }

    private ServerTroop[] getAndValidateAttackerTroops(String[] attackerCardIds, ServerTroop defenderTroop) throws ClientException {
        ServerTroop[] attackerTroops = new ServerTroop[attackerCardIds.length];
        for (int i = 0; i < attackerTroops.length; i++) {
            attackerTroops[i] = getCurrentTurnPlayer().getTroop(attackerCardIds[i]);
            if (attackerTroops[i] == null) {
                throw new ClientException("invalid attacker troop");
            }

            checkRangeForAttack(attackerTroops[i], defenderTroop);
        }
        return attackerTroops;
    }

    private void checkRangeForAttack(ServerTroop attackerTroop, ServerTroop defenderTroop) throws ClientException {
        if (attackerTroop.getCard().getAttackType() == AttackType.MELEE) {
            if (!attackerTroop.getCell().isNearbyCell(defenderTroop.getCell())) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        } else if (attackerTroop.getCard().getAttackType() == AttackType.RANGED) {
            if (attackerTroop.getCell().isNearbyCell(defenderTroop.getCell()) ||
                    attackerTroop.getCell().manhattanDistance(defenderTroop.getCell()) > attackerTroop.getCard().getRange()) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        } else { // HYBRID
            if (attackerTroop.getCell().manhattanDistance(defenderTroop.getCell()) > attackerTroop.getCard().getRange()) {
                throw new ClientException(attackerTroop.getCard().getCardId() + " can not attack to this target");
            }
        }
    }

    private void damageFromAllAttackers(ServerTroop defenderTroop, ServerTroop[] attackerTroops) {
        for (ServerTroop attackerTroop : attackerTroops) {
            if (defenderTroop.canGiveBadEffect() &&
                    (defenderTroop.canBeAttackedFromWeakerOnes() || attackerTroop.getCurrentAp() > defenderTroop.getCurrentAp())
            ) {
                damage(attackerTroop, defenderTroop);

                attackerTroop.setCanAttack(false);
                attackerTroop.setCanMove(false);
                GameServer.getInstance().sendTroopUpdateMessage(this, attackerTroop);

                applyOnAttackSpells(attackerTroop, defenderTroop);
            }
        }
    }

    public abstract boolean finishCheck();

    void finish() {
        this.cancelTimeLimit();
        this.isFinished = true;
    }

    private void applySpell(Spell spell, TargetData target) {
        spell.setLastTurnUsed(turnNumber);
        Buff buff = new Buff(spell.getAction(), target);
        GameServer.getInstance().sendSpellMessage(this, target, spell.getAvailabilityType());
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
            GameServer.getInstance().sendGameUpdateMessage(this);
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
        ArrayList<ServerTroop> inCellTroops = getInCellTargetTroops(cells);
        Buff troopBuff = new Buff(
                buff.getAction().makeCopyAction(1, 0), new TargetData(inCellTroops)
        );
        tempBuffs.add(troopBuff);
        applyBuffOnTroops(troopBuff, inCellTroops);
    }

    private void applyBuffOnTroops(Buff buff, List<ServerTroop> targetTroops) {
        SpellAction action = buff.getAction();
        for (ServerTroop troop : targetTroops) {
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
                troop.setCanAttack(false);
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

            GameServer.getInstance().sendTroopUpdateMessage(this, troop);
        }
    }

    private void removePositiveBuffs(ServerTroop troop) {
        for (Buff buff : buffs) {
            if (buff.isPositive() && buff.getAction().getDuration() >= 0) {
                buff.getTarget().getTroops().remove(troop);
            }
        }
    }

    private void removeNegativeBuffs(ServerTroop troop) {
        for (Buff buff : buffs) {
            if (!buff.isPositive() && buff.getAction().getDuration() >= 0) {
                buff.getTarget().getTroops().remove(troop);
            }
        }
    }

    void killTroop(ServerTroop troop) {
        applyOnDeathSpells(troop);
        if (troop.getPlayerNumber() == 1) {
            playerOne.killTroop(this, troop);
            gameMap.removeTroop(troop);
        } else if (troop.getPlayerNumber() == 2) {
            playerTwo.killTroop(this, troop);
            gameMap.removeTroop(troop);
        }
        GameServer.getInstance().sendChangeCardPositionMessage(this, troop.getCard(), CardPosition.GRAVE_YARD);
    }

    private void applyOnDeathSpells(ServerTroop troop) {
        for (Spell spell : troop.getCard().getSpells()) {
            if (spell.getAvailabilityType().isOnDeath())
                applySpell(
                        spell,
                        detectTarget(spell, troop.getCell(), gameMap.getCell(0, 0), getOtherTurnPlayer().getHero().getCell())
                );
        }
    }

    private ArrayList<ServerTroop> getInCellTargetTroops(List<Cell> cells) {
        ArrayList<ServerTroop> inCellTroops = new ArrayList<>();
        for (Cell cell : cells) {
            ServerTroop troop = playerOne.getTroop(cell);
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
            Cell centerCell = getCenterPosition(spell, cardCell, clickCell, heroCell);
            ArrayList<Cell> targetCells = detectCells(centerCell, spell.getTarget().getDimensions());
            addTroopsAndCellsToTargetData(spell, targetData, player, targetCells);
        }

    }

    private Cell getCenterPosition(Spell spell, Cell cardCell, Cell clickCell, Cell heroCell) {
        Cell centerCell;
        if (spell.getTarget().isRelatedToCardOwnerPosition()) {
            centerCell = new Cell(cardCell.getRow(), cardCell.getColumn());
        } else if (spell.getTarget().isForAroundOwnHero()) {
            centerCell = new Cell(heroCell.getRow(), heroCell.getColumn());
        } else {
            centerCell = new Cell(clickCell.getRow(), clickCell.getColumn());
        }
        return centerCell;
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
                ServerTroop troop = player.getTroop(cell);
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

    private void addTroopToTargetData(Spell spell, TargetData targetData, ServerTroop troop) {
        if (spell.getTarget().getCardType().isHero() && troop.getCard().getType() == CardType.HERO) {
            targetData.getTroops().add(troop);
        }
        if (spell.getTarget().getCardType().isMinion() && troop.getCard().getType() == CardType.MINION) {
            targetData.getTroops().add(troop);
        }
    }

    private ArrayList<Cell> detectCells(Cell centerCell, Cell dimensions) {

        ArrayList<Cell> targetCells = new ArrayList<>();

        // This fixes a bug in the previous logic;
        // Previously 3x3 square on the edges/corners of the board gave incorrect result.
        if (dimensions.getRow() % 2 != 0 && dimensions.getColumn() % 2 != 0) {
            int rowMin = centerCell.getRow() - (dimensions.getRow() / 2);
            int rowMax = centerCell.getRow() + (dimensions.getRow() / 2);

            int colMin = centerCell.getColumn() - (dimensions.getColumn() / 2);
            int colMax = centerCell.getColumn() + (dimensions.getColumn() / 2);

            for (int i = rowMin; i <= rowMax; i++) {
                for (int j = colMin; j <= colMax; j++) {
                    if (gameMap.isInMap(i, j)) {
                        targetCells.add(gameMap.getCells()[i][j]);
                    }
                }
            }
        } else {
            int firstRow = calculateFirstCoordinate(centerCell.getRow(), dimensions.getRow());
            int firstColumn = calculateFirstCoordinate(centerCell.getColumn(), dimensions.getColumn());

            int lastRow = calculateLastCoordinate(firstRow, dimensions.getRow(), GameMap.getNumRows());
            int lastColumn = calculateLastCoordinate(firstColumn, dimensions.getColumn(), GameMap.getNumColumns());
            for (int i = firstRow; i < lastRow; i++) {
                for (int j = firstColumn; j < lastColumn; j++) {
                    if (gameMap.isInMap(i, j))
                        targetCells.add(gameMap.getCells()[i][j]);
                }
            }
        }

        // Debugging print statements
        //GameServer.serverPrint("( " + centerPosition.toString() + ") (" + dimensions.toString() + ")");
        //targetCells.forEach((n) -> System.out.print("[" + n.getRow() + n.getColumn() + "] "));
        //System.out.println();
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

    public List<CellEffect> getCellEffects() {
        List<CellEffect> result = new ArrayList<>();

        buffs.forEach(buff -> buff.getTarget().getCells()
                .forEach(cell -> result.add(new CellEffect(new Cell(cell.getRow(), cell.getColumn()), buff.isPositive())))
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