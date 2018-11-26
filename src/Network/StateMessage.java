package Network;

import GameObjects.Cell.Cell;
import GameTools.GameLogic;

import java.io.Serializable;
import java.util.ArrayList;

/*
Represents all the messages that the server send to the player.
 */
public class StateMessage implements Serializable {

    private ArrayList<Cell> cells;
    private GameLogic gameLogic;
    public boolean isGameOver;
    public int playerId = -1;

    private static final long serialVersionUID = 1L;

    public StateMessage(GameLogic gameLogic, ArrayList<Cell> cells) {
        this.gameLogic = gameLogic;
        this.cells = cells;
    }

    public StateMessage(boolean b) {
        isGameOver = b;
    }

    public StateMessage(int playerId) {
        this.playerId = playerId;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
