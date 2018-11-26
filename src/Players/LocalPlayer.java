package Players;

import GameObjects.Cell.*;
import Sketches.PlayerSketch;
import processing.core.PVector;

import java.util.ArrayList;

public class LocalPlayer extends Player {

    // Ready Button
    public final float x = sketch.WORLD_WIDTH / 2 + 80;
    public final float y = 650;
    public final int boxWidth = 200;
    public final int boxHeight = 100;

    Cell player;
    ArrayList<Cell> cells;

    public LocalPlayer(PlayerSketch sketch) {
        super(sketch);
    }

    public void setup() {
        cells = new ArrayList<>();

        // create the chosen player.
        createPlayer();

        // In case of AI we set it up here.
        cells.add(player);

        // Setting up the blobs and materials.
        sketch.getGameLogic().setupBlobs();
        sketch.getGameLogic().setupHerbs();
        sketch.getGameLogic().setupWood();
        sketch.getGameLogic().setupSteel();
    }

    private void createPlayer() {
        if (currentCell == RIVEN)
            player = new RivenCell(sketch, sketch.random(-sketch.width, sketch.width),
                    sketch.random(-sketch.height, sketch.height), 0, 0, 35, 4,
                    0, nickname, sketch.getGameLogic().getShrooms());
        else if (currentCell == JAX)
            player = new JaxCell(sketch, sketch.random(-sketch.width, sketch.width),
                    sketch.random(-sketch.height, sketch.height), 0, 0, 35, 4,
                    0, nickname);
        else if (currentCell == LULU)
            player = new LuluCell(sketch, sketch.random(-sketch.width, sketch.width),
                    sketch.random(-sketch.height, sketch.height), 0, 0, 35, 4,
                    0, nickname);
        else if (currentCell == ZED)
            player = new ZedCell(sketch, sketch.random(-sketch.width, sketch.width),
                    sketch.random(-sketch.height, sketch.height), 0, 0, 35, 4,
                    0, nickname);
    }

    // Generate local's player start screen.
    public void localStartScreen() {
        sketch.background(0);
        sketch.textAlign(sketch.CENTER);
        sketch.fill(255);
        sketch.textSize(52);
        sketch.text("Blobby", sketch.width / 2, sketch.height / 2 - 300);
        sketch.textSize(20);
        sketch.textAlign(0);
        cellSelection();
        sketch.getGameDrawer().drawButton("Start", x, y, boxWidth, boxHeight);
    }

    public void draw() {
        sketch.background(255);

        if (sketch.getGameLogic().getIsGameOver()) {
            sketch.setCurrentScreen(sketch.GAME_OVER_SCREEN);
            return;
        }

        sketch.pushMatrix();
        sketch.centreCamera(player);
        player.integrate(new PVector(sketch.mouseX, sketch.mouseY));

        // Integrate all cells in case of AI.


        // Detect collisions between all cells, blobs and materials.
        sketch.getGameLogic().detectCellBlobCollections(cells);
        sketch.getGameLogic().detectCellMaterialCollections(cells);
        sketch.getGameLogic().detectCellCollisions2(cells);

        sketch.getGameDrawer().drawGameObjects(cells, sketch.getGameLogic().getBlobs(),
                sketch.getGameLogic().getMaterials(), sketch.getGameLogic().getShrooms());

        sketch.popMatrix();

        sketch.getGameDrawer().drawGameUI(player, cells);

        sketch.getGameLogic().generateBlobsAndMaterials();
    }

    public Cell getPlayer() {
        return player;
    }
}
