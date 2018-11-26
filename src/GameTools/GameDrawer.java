package GameTools;

import GameObjects.Blob;
import GameObjects.Cell.*;
import GameObjects.Inventory;
import GameObjects.Materials.Herb;
import GameObjects.Materials.Material;
import GameObjects.Materials.Steel;
import GameObjects.Materials.Wood;
import GameObjects.Shroom;
import Players.ClientPlayer;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;

public class GameDrawer {

    private PApplet p;
    private PImage food;
    private PImage poisonSquare;
    private PImage shieldSquare;
    private PImage spikesSquare;
    private PImage rivenUltimate;
    private PImage zedUltimate;
    private PImage jaxUltimate;
    private PImage luluUltimate;
    private PImage herbImage;
    private PImage woodImage;
    private PImage steelImage;

    public GameDrawer(PApplet p) {
        this.p = p;
        food = p.loadImage("Resources/food_blue.png");
        poisonSquare = p.loadImage("Resources/poison.png");
        shieldSquare = p.loadImage("Resources/shield.png");
        spikesSquare = p.loadImage("Resources/spikes.png");
        rivenUltimate = p.loadImage("Resources/riven-ultimate.png");
        zedUltimate = p.loadImage("Resources/zed-ultimate.png");
        jaxUltimate = p.loadImage("Resources/jax-ultimate.png");
        luluUltimate = p.loadImage("Resources/lulu-ultimate.png");
        herbImage = p.loadImage("Resources/herb.png");
        woodImage = p.loadImage("Resources/wood.png");
        steelImage = p.loadImage("Resources/steel.png");

    }

    public void drawGameObjects(ArrayList<Cell> cells, ArrayList<Blob> blobs, ArrayList<Material> materials,
                                ArrayList<Shroom> shrooms) {
        drawCells(cells);
        drawBlob(blobs);
        drawMaterials(materials);
        drawShrooms(shrooms);
    }

    public void drawGameUI(Cell cell, ArrayList<Cell> cells) {
        drawMiniMap(cell);
        drawInventory(cell);
        drawLeaderBoard(cells, cell);
    }

    private void drawShrooms(ArrayList<Shroom> shrooms) {
        for (Shroom shroom : shrooms) {
            p.fill(0, 255, 0);
            p.ellipse(shroom.getPosition().x, shroom.getPosition().y, shroom.getDiameter(), shroom.getDiameter());
        }
    }

    private void drawCell(Cell cell) {

        p.stroke(0);

        if (cell.getIsSpiked()) {
            p.stroke(0);
            // Go through the circle every 45 degrees.
            for (float i = 0; i < 360; i += 45) {
                // get the x and y positions of the spikes.
                float x = cell.getPosition().x + cell.getDetectionRadius() * PApplet.cos(PApplet.radians(i));
                float y = cell.getPosition().y + cell.getDetectionRadius() * PApplet.sin(PApplet.radians(i));
                p.rectMode(p.CENTER);
                p.fill(0);
                // drawing the spikes. It grows with the darius.
                p.rect(x, y, cell.getRadius() / 2, cell.getRadius() / 2);
            }
        }

        if (cell instanceof RivenCell)
            p.fill(0, 255, 0);
        if (cell instanceof LuluCell)
            p.fill(255, 255, 0);
        if (cell instanceof ZedCell)
            p.fill(255, 0, 0);
        if (cell instanceof JaxCell)
            p.fill(255, 0, 255);

        p.ellipse(cell.getPosition().x, cell.getPosition().y, cell.getDiameter(), cell.getDiameter());
        drawNickname(cell);

        if (cell.getIsPoisonous()) {
            p.fill(255, 105, 180, 200);
            p.ellipse(cell.getPosition().x, cell.getPosition().y, cell.getDiameter() - 5, cell.getDiameter() - 5);
        } else if (cell.getIsShielded()) {
            p.fill(0, 0, 255, 200);
            p.ellipse(cell.getPosition().x, cell.getPosition().y, cell.getDiameter() - 5, cell.getDiameter() - 5);
        }

        p.rectMode(p.CORNER);
    }

    private void drawCells(ArrayList<Cell> cells) {
        for (Cell cell : cells) {
            drawCell(cell);
        }
    }

    private void drawBlob(ArrayList<Blob> blobs) {
        for (Blob blob : blobs) {
            p.fill(128, 0, 128);

            // This part was re-used from my first submission. This is used in order to be use images.
            p.textureMode(p.NORMAL);
            p.beginShape();
            p.noTint();
            p.noStroke();
            p.texture(food);
            for (int i = 0; i < 20; i++) {
                float stepFac = 2 * p.PI * ((float) i / 20);
                p.vertex(blob.getPosition().x + blob.getRadius() * PApplet.sin(stepFac),
                        blob.getPosition().y + blob.getRadius() * PApplet.cos(stepFac),
                        (PApplet.sin(stepFac) + 1) / 2, (PApplet.cos(stepFac) + 1) / 2);
            }
            p.endShape(p.CLOSE);
        }
    }

    public void eatenBlobScore(Cell cell) {
        p.fill(0);
        p.text(cell.getEatenBlobs(), cell.getPosition().x - 5, cell.getPosition().y + cell.getRadius());
        p.text(cell.getSpeed(), cell.getPosition().x + 5, cell.getPosition().y + cell.getRadius());
    }

    private void drawNickname(Cell cell) {
        p.fill(0);
        p.textSize(cell.getRadius() / 2);
        p.textAlign(p.CENTER, p.CENTER);
        p.text(cell.getNickname(), cell.getPosition().x, cell.getPosition().y);
    }

    private void drawMiniMap(Cell c) {
        p.stroke(0);

        p.fill(255, 0, 0, 127);
        // the minimap rectangle.
        p.rect(17, 18, 89, 82);

        // Get the proportion of the Mini-map relative to the actual map.
        float x = 85f / (p.width * 2f) * c.getPosition().x;
        float y = 80f / (p.height * 2f) * c.getPosition().y;

        p.fill(124, 252, 0);
        // Circle represents the cell.
        p.ellipse(20 + 85 / 2 + x, 20 + 80 / 2 + y, 5, 5);
    }

    private void drawInventory(Cell cell) {
        p.textSize(10);
        p.stroke(0);

        // right rectangle
        p.fill(0, 0, 0, 170);
        p.rect(p.width / 2 - 150, p.height - 80, 300, 80);

        // left rectangle
        p.fill(0, 0, 0, 170);
        p.rect(p.width / 2 - 220, p.height - 80, 70, 80);

        p.fill(255);
        // GameObjects.Inventory Count
        p.textAlign(p.LEFT);
        p.textSize(15);
        p.text("H:" + cell.getHerbCount(), p.width / 2 - 210, p.height - 60);
        p.text("W:" + cell.getWoodCount(), p.width / 2 - 210, p.height - 38);
        p.text("S:" + cell.getSteelCount(), p.width / 2 - 210, p.height - 15);
        p.textAlign(p.CENTER);

        p.stroke(255);
        p.image(poisonSquare, p.width / 2 - 130, p.height - 70, 60, 60);
        p.image(shieldSquare, p.width / 2 - 130 + 70, p.height - 70, 60, 60);
        p.image(spikesSquare, p.width / 2 - 130 + 2 * 70, p.height - 70, 60, 60);

        PImage ultimate = null;

        if (cell instanceof JaxCell)
            ultimate = jaxUltimate;
        else if (cell instanceof RivenCell)
            ultimate = rivenUltimate;
        else if (cell instanceof ZedCell)
            ultimate = zedUltimate;
        else if (cell instanceof LuluCell)
            ultimate = luluUltimate;

        p.image(ultimate, p.width / 2 - 130 + 3 * 70, p.height - 70, 60, 60);

        drawCooldowns(cell);
    }

    private void drawCooldowns(Cell cell) {
        if (cell.getIsPoisonous()) {
            float y = PApplet.map(cell.getPoisonTimer(), 0, Inventory.POISON_TIMER, 0, 60);
            p.fill(255, 107);
            p.rect(p.width / 2 - 130, p.height - 70 + y, 60, 60 - y);
        }
        if (cell.getIsShielded()) {
            float y = PApplet.map(cell.getShieldTimer(), 0, Inventory.SHIELD_TIMER, 0, 60);
            p.fill(255, 107);
            p.rect(p.width / 2 - 130 + 70, p.height - 70 + y, 60, 60 - y);
        }
        if (cell.getIsSpiked()) {
            float y = PApplet.map(cell.getSpikeTimer(), 0, Inventory.SPIKE_TIMER, 0, 60);
            p.fill(255, 107);
            p.rect(p.width / 2 - 130 + 2 * 70, p.height - 70 + y, 60, 60 - y);
        }
        if (cell.getUltimateTimer() > 0) {
            float y = PApplet.map(cell.getUltimateTimer(), 0, cell.getUltimateCoolDown(), 0, 60);
            p.fill(255, 107);
            p.rect(p.width / 2 - 130 + 3 * 70, p.height - 70 + y, 60, 60 - y);
        }
    }

    private void drawMaterials(ArrayList<Material> materials) {
        p.stroke(0);
        PImage image = null;

        for (Material material : materials) {
            if (material instanceof Herb)
                image = herbImage;
            else if (material instanceof Wood)
                image = woodImage;
            else if (material instanceof Steel)
                image = steelImage;

            p.textureMode(p.NORMAL);
            p.beginShape();
            p.noTint();
            p.noStroke();
            p.texture(image);
            for (int i = 0; i < 20; i++) {
                float stepFac = 2 * p.PI * ((float) i / 20);
                p.vertex(material.getPosition().x + material.getRadius() * PApplet.sin(stepFac),
                        material.getPosition().y + material.getRadius() * PApplet.cos(stepFac),
                        (PApplet.sin(stepFac) + 1) / 2, (PApplet.cos(stepFac) + 1) / 2);
            }
            p.endShape(p.CLOSE);
        }
    }

    public void drawStartButton(boolean isReady, float x, float y, int width, int height) {
        p.textAlign(p.CENTER);
        String text = "";

        if (isReady)
            text = "Ready";
        else
            text = "Start";

        p.fill(255, 255, 255);
        p.rect(x, y, width, height);
        p.fill(0, 0, 0);
        p.text(text, x + width / 2, y + height / 2);

    }

    private void drawLeaderBoard(ArrayList<Cell> cells, Cell currentCell) {
        // To sort the arrayList based on the number of blobs.
        Collections.sort(cells);
        p.stroke(0);
        p.fill(255, 0, 0, 127);
        p.rect(700, 10, 140, 150);

        p.textAlign(p.LEFT);
        p.textSize(15);

        // Draws the top 5.
        for (int j = 0; j < cells.size(); j++) {
            p.fill(0);
            if (j < 5) {
                if (cells.get(j).getPlayerId() == currentCell.getPlayerId())
                    p.fill(255);
                p.text((j + 1) + ") " + cells.get(j).getNickname() + ": " + cells.get(j).getEatenBlobs(), 700 + 10, 30 + (20 * j));
            } else if (cells.get(j).getPlayerId() == currentCell.getPlayerId()) {
                p.fill(255);
                p.text((j + 1) + ") " + cells.get(j).getNickname() + ": " + cells.get(j).getEatenBlobs(), 700 + 10, 30 + (20 * 5));
            }
        }
    }

    public void drawButton(String text, float x, float y, float width, float height) {
        p.textAlign(p.CENTER);
        p.textSize(22);
        p.fill(255, 255, 255);
        p.rect(x, y, width, height);
        p.fill(0, 0, 0);
        p.text(text, x + width / 2, y + height / 2);
    }

    public void drawCellSelection(int cellType) {
        if (cellType == ClientPlayer.RIVEN) {
            p.fill(0, 255, 0);
            p.ellipse(p.width / 2 + 180, p.height / 2 - 100, 200, 200);
        } else if (cellType == ClientPlayer.LULU) {
            p.fill(255, 255, 0);
            p.ellipse(p.width / 2 + 180, p.height / 2 - 100, 200, 200);
        } else if (cellType == ClientPlayer.JAX) {
            p.fill(255, 0, 255);
            p.ellipse(p.width / 2 + 180, p.height / 2 - 100, 200, 200);
        } else if (cellType == ClientPlayer.ZED) {
            p.fill(255, 0, 0);
            p.ellipse(p.width / 2 + 180, p.height / 2 - 100, 200, 200);
        }
    }

    public void drawCellDescription(int cellType) {
        p.fill(255);
        p.textSize(32);

        if (cellType == ClientPlayer.RIVEN) {
            p.text("RIVEN", 50, p.height / 2 - 95);
            p.textSize(20);
            p.textAlign(p.LEFT);
            p.text("R: ", 5, p.height / 2 - 65);
            p.text("Riven drops shroom that can kill the opponent.", 5, p.height / 2 - 45);
            p.textAlign(p.CENTER);
        } else if (cellType == ClientPlayer.JAX) {
            p.text("JAX", 40, p.height / 2 - 95);
            p.textSize(20);
            p.textAlign(p.LEFT);
            p.text("R: ", 10, p.height / 2 - 65);
            p.text("Jax gets enough resources to craft poison,", 5, p.height / 2 - 45);
            p.text("shield and spikes.", 5, p.height / 2 - 25);
            p.textAlign(p.CENTER);
        } else if (cellType == ClientPlayer.LULU) {
            p.text("LULU", 50, p.height / 2 - 95);
            p.textSize(20);
            p.textAlign(p.LEFT);
            p.text("R: ", 10, p.height / 2 - 65);
            p.text("Lulu will get bigger in size for 50 frames.", 5, p.height / 2 - 45);
            p.textAlign(p.CENTER);
        } else if (cellType == ClientPlayer.ZED) {
            p.text("ZED", 40, p.height / 2 - 95);
            p.textSize(20);
            p.textAlign(p.LEFT);
            p.text("R: ", 10, p.height / 2 - 65);
            p.text("Zed will double in speed for 50 frames.", 5, p.height / 2 - 45);
            p.textAlign(p.CENTER);
        }
    }
}
