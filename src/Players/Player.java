package Players;

import Sketches.PlayerSketch;

public class Player {

    PlayerSketch sketch;

    // Arrow right
    public final float xR = 850 / 2 + 350;
    public final float yR = 800 / 2 - 115;
    public final int widthR = 30;
    public final int heightR = 30;

    // left right
    public final float xL = 850 / 2 - 40;
    public final float yL = 800 / 2 - 115;
    public final int widthL = 30;
    public final int heightL = 30;

    public static final int RIVEN = 0;
    public static final int ZED = 1;
    public static final int JAX = 2;
    public static final int LULU = 3;

    int currentCell;


    boolean playerIsReady = false;
    public String nickname = "";

    Player(PlayerSketch sketch) {
        this.sketch = sketch;
    }

    public void setup() {
    }

    public void draw() {
    }

    public boolean getPlayerIsReady() {
        return playerIsReady;
    }

    // Generates the cell selection menu.
    void cellSelection() {
        sketch.getGameDrawer().drawCellSelection(currentCell);
        sketch.fill(255);
        sketch.rect(xR, yR, widthR, heightR);
        sketch.rect(xL, yL, widthL, heightL);
        sketch.fill(0);
        sketch.text(">", sketch.width / 2 + 365, sketch.height / 2 - 95);
        sketch.text("<", sketch.width / 2 - 30, sketch.height / 2 - 95);
        sketch.getGameDrawer().drawCellDescription(currentCell);
    }

    public void setPlayerIsReady(boolean value) {
        this.playerIsReady = value;
    }


    //***************************************************
    //          For Cell Selection menu
    //***************************************************
    public void incrementCurrentCell() {
        if (currentCell == 3)
            currentCell = 0;
        else currentCell++;
    }

    public void decrementCurrentCell() {
        if (currentCell == 0)
            currentCell = 3;
        else currentCell--;
    }


}
