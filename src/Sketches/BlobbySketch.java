package Sketches;

import GameObjects.Cell.Cell;
import GameTools.GameDrawer;
import GameTools.GameLogic;
import processing.core.PApplet;

import java.util.ArrayList;

public class BlobbySketch extends PApplet {


    //SERVER IP and Port
    public int serverPort = 6000;
    public String serverIp = "localhost";


    GameLogic gameLogic;
    GameDrawer gameDrawer;

    private float scaleFactor = 1;

    public final float WORLD_WIDTH = 550;
    public final float WORLD_HEIGHT = 500;

    // Current screen we are on.
    int currentScreen = 0;

    // The cells in the game.
    ArrayList<Cell> playerCells = new ArrayList<>();

    void setupGameLogic(BlobbySketch blobbySketch) {
        gameLogic = new GameLogic(blobbySketch);
        gameDrawer = new GameDrawer(blobbySketch);
    }

    public void setup() {
    }

    public void draw() {
    }

    Cell getPlayerCell(int playerId) {
        for (Cell cell : playerCells) {
            if (cell.getPlayerId() == playerId)
                return cell;
        }
        return null;
    }

    /*
    The screen the shows that the player is dead.
    */
    void gameOverScreen() {
        background(0);
        fill(255);
        textAlign(CENTER, CENTER);
        text("You Died", width / 2, height / 2);
    }

    /*
    Makes sure that the player is always in the centre of the screen.
     */
    public void centreCamera(Cell cell) {
        // to make sure that the players cell is in the middle.
        translate(width / 2, height / 2);
        // TO gradually position the player.
        scaleFactor = lerp(scaleFactor, 50 / cell.getRadius(), 0.1f);
        scale(scaleFactor);
        translate(-cell.getPosition().x, -cell.getPosition().y);
    }

    //******************************************************************
    //                      Getters and Setters
    //******************************************************************

    public GameDrawer getGameDrawer() {
        return gameDrawer;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public ArrayList<Cell> getPlayerCells() {
        return playerCells;
    }

    public void setPlayerCells(ArrayList<Cell> cells) {
        playerCells = cells;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setCurrentScreen(int value) {
        currentScreen = value;
    }

    public void settings() {
        size(850, 800, P2D);
    }

}
