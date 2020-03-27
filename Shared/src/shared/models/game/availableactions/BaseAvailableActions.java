package shared.models.game.availableactions;

import shared.models.card.Card;
import shared.models.game.BaseGame;
import shared.models.game.BasePlayer;
import shared.models.game.Troop;
import shared.models.game.map.BaseGameMap;
import shared.models.game.map.Cell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class BaseAvailableActions<InsertType extends BaseInsert, AttackType extends BaseAttack, MoveType extends BaseMove, TroopType extends Troop, GameMapType extends BaseGameMap<TroopType>, GameType extends BaseGame<PlayerType, GameMapType>, CardType extends Card, PlayerType extends BasePlayer<CardType, TroopType>> {
  protected final List<InsertType> handInserts = new ArrayList<>();
  protected final List<AttackType> attacks = new ArrayList<>();
  protected final List<MoveType> moves = new ArrayList<>();
  private final Constructor<MoveType> moveConstructor;

  public List<InsertType> getHandInserts() {
    return Collections.unmodifiableList(handInserts);
  }

  public List<AttackType> getAttacks() {
    return Collections.unmodifiableList(attacks);
  }

  public List<MoveType> getMoves() {
    return Collections.unmodifiableList(moves);
  }


  public BaseAvailableActions(Constructor<MoveType> moveConstructor) {
    this.moveConstructor = moveConstructor;

  }



  public void calculateMoves(GameType game) {
    PlayerType ownPlayer = game.getCurrentTurnPlayer();
    moves.clear();
    for (TroopType troop : ownPlayer.getTroops()) {
      ArrayList<Cell> troopMoves = calculateAvailableMovesForTroop(game, troop);

      if (troopMoves.size() > 0) {
        moves.add(this.constructMove(troop, troopMoves));
      }
    }
  }


  protected ArrayList<Cell> calculateAvailableMovesForTroop(GameType game, Troop troop) {
    Cell troopCell = troop.getCell();

    HashSet<Cell> walkableCells = new HashSet<>(); // Cells which the unit can move to.
    // The starting cell is not a walkable cell by convention.

    boolean isProvoked = getIsProvoked(game, troopCell);
    if (isProvoked || !troop.canMove()) {
      return new ArrayList<>(walkableCells);
    }

    HashSet<Cell> seenCells = new HashSet<>();
    seenCells.add(troopCell);

    int moveSpeed = 2; // Todo make a troop property.

    // Cells which the unit can move through.
    ArrayList<Cell> nextPathableFrontier = new ArrayList<>();
    nextPathableFrontier.add(troop.getCell());

    for (int distance = 0; distance < moveSpeed; distance++) {
      ArrayList<Cell> pathableFrontier = nextPathableFrontier;
      nextPathableFrontier = new ArrayList<>();
      for (Cell currentCell : pathableFrontier) {
        ArrayList<Cell> manhattanAdjacentCells =
            game.getGameMap().getManhattanAdjacentCells(currentCell);
        for (Cell adjacentCell : manhattanAdjacentCells) {
          Troop troopInSpace = game.getGameMap().getTroopAtLocation(adjacentCell);

          boolean blockedByAnything = troopInSpace != null;
          if (!blockedByAnything) {
            if (!seenCells.contains(adjacentCell)) {
              walkableCells.add(adjacentCell);
            }
          }

          boolean blockedByEnemy = blockedByAnything
              && troopInSpace.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber();
          if (!blockedByEnemy) {
            nextPathableFrontier.add(adjacentCell);
          }
          seenCells.add(adjacentCell);
        }
      }
    }

    return new ArrayList<>(walkableCells);
  }


  protected boolean getIsProvoked(GameType game, Cell troopCell) {
    boolean isProvoked = false;
    List<Cell> neighbourCells = game.getGameMap().getNearbyCells(troopCell);
    for (Cell nCell : neighbourCells) {
      if (game.getGameMap().getTroopAtLocation(nCell) != null) {
        Troop nearbyUnit = game.getGameMap().getTroopAtLocation(nCell);
        // is provoked?
        if (nearbyUnit.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber()
            && nearbyUnit.getCard().getDescription().contains("Provoke")) {
          isProvoked = true;
          break;
        }
      }
    }
    return isProvoked;
  }

  private MoveType constructMove(TroopType troop, ArrayList<Cell> targets) {
    MoveType move = null;
    try {
      move = moveConstructor.newInstance(troop, targets);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      assert false;
    }
    return move;
  }


}
