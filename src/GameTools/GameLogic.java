package GameTools;

import GameObjects.Blob;
import GameObjects.Cell.Cell;
import GameObjects.Materials.Herb;
import GameObjects.Materials.Material;
import GameObjects.Materials.Steel;
import GameObjects.Materials.Wood;
import GameObjects.Shroom;
import Network.PlayerMessage;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import static processing.core.PApplet.sqrt;

public class GameLogic implements Serializable {

    private transient PApplet p;
    private ArrayList<Blob> blobs;
    private ArrayList<Material> materials;
    private ArrayList<Shroom> shrooms;

    private int woodCount;
    private int steelCount;
    private int herbCount;

    // GameObjects Generation Timer
    private int blobTimer = 30;
    private int woodTimer = 500;
    private int herbTimer = 500;
    private int steelTimer = 500;

    private boolean isGameOver = false;


    public GameLogic(PApplet p) {
        this.p = p;
        blobs = new ArrayList<>();
        materials = new ArrayList<>();
        shrooms = new ArrayList<>();
    }

    //****************************
    //          Setup
    //****************************
    public void setupBlobs() {
        for (int i = 0; i < 100; i++) {
            float x = p.random(-p.width, p.width);
            float y = p.random(-p.height, p.height);
            Blob blob = new Blob(p, x, y, 0, 0, 10, 10);
            blobs.add(blob);
        }
    }

    public void setupHerbs() {
        for (int i = 0; i < 15; i++) {
            float x = p.random(-p.width, p.width);
            float y = p.random(-p.height, p.height);
            Herb herb = new Herb(p, x, y, 0, 0, 20, 10);
            materials.add(herb);
            herbCount++;
        }
    }

    public void setupWood() {
        for (int i = 0; i < 15; i++) {
            float x = p.random(-p.width, p.width);
            float y = p.random(-p.height, p.height);
            Wood wood = new Wood(p, x, y, 0, 0, 20, 10);
            materials.add(wood);
            woodCount++;
        }
    }

    public void setupSteel() {
        for (int i = 0; i < 15; i++) {
            float x = p.random(-p.width, p.width);
            float y = p.random(-p.height, p.height);
            Steel steel = new Steel(p, x, y, 0, 0, 20, 10);
            materials.add(steel);
            steelCount++;
        }
    }


    //********************************************************
    //              Collisions (Blob + Materials)
    //********************************************************
    private void detectCellBlobCollision(Cell cell) {
        Iterator<Blob> it = blobs.iterator();
        while (it.hasNext()) {
            Blob blob = it.next();
            // get the distance
            if (PVector.dist(blob.getPosition(), cell.getPosition()) < blob.getRadius() + cell.getRadius()) {
                // increase the size of the cell.
                growCell(cell, blob);
                // increment the eatenblobs.
                cell.incrementEatenBlobs();
                // reduce speed.
                cell.reduceSpeed();
                it.remove();
            }
        }
    }

    private void detectMaterialCollision(Cell cell) {
        Iterator<Material> it = materials.iterator();
        while (it.hasNext()) {
            Material material = it.next();

            // get the distance between material and cell.
            if (PVector.dist(material.getPosition(), cell.getPosition()) < material.getRadius() + cell.getRadius()) {
                cell.addToInventory(material);
                updateMaterialCounters(material);
                it.remove();
            }
        }
    }

    // Checks what type of material it is and updates server accordingly.
    private void updateMaterialCounters(Material material) {
        if (material instanceof Wood)
            woodCount--;
        else if (material instanceof Steel)
            steelCount--;
        else if (material instanceof Herb)
            herbCount--;
    }

    public void detectCellBlobCollections(ArrayList<Cell> cells) {
        for (Cell cell : cells)
            detectCellBlobCollision(cell);
    }

    public void detectCellMaterialCollections(ArrayList<Cell> cells) {
        for (Cell cell : cells)
            detectMaterialCollision(cell);
    }

    private void growCell(Cell cell, Blob blob) {
        // adding the area of the cell and blob.
        float cellArea = cell.getRadius() * cell.getRadius() * p.PI;
        float blobArea = blob.getRadius() * blob.getRadius() * p.PI;
        float newArea = cellArea + blobArea;
        // getting the radius from the area.
        cell.setDiameter(sqrt(newArea / p.PI) * 2);
    }

    //********************************************************
    //              Collisions (Cells)
    //********************************************************

    public ArrayList<Cell> detectCellCollisions2(ArrayList<Cell> cells) {
        ArrayList<Cell> toRemove = new ArrayList<>();

        for (int i = 0; i < cells.size(); i++) {
            // in case it is already deleted.
            if (toRemove.contains(cells.get(i)))
                continue;
            for (int j = i + 1; j < cells.size(); j++) {
                // in case it is already deleted.
                if (toRemove.contains(cells.get(j)))
                    continue;

                detectCellCollision2(cells.get(i), cells.get(j), toRemove);
            }
        }
        cells.removeAll(toRemove);
        return toRemove;
    }

    private void detectCellCollision2(Cell cell1, Cell cell2, ArrayList<Cell> toRemove) {


        if (cell1.getIsSpiked() || cell2.getIsSpiked()) {
            handleSpikeCollision(cell1, cell2, toRemove);
            return;
        }

        // if in collision range (a bit overlapping).
        if (PVector.dist(cell1.getPosition(), cell2.getPosition()) <= cell1.getDetectionRadius() ||
                PVector.dist(cell1.getPosition(), cell2.getPosition()) <= cell2.getDetectionRadius()) {
            // check if any has poison.
            if (cell1.getIsPoisonous() || cell2.getIsPoisonous())
                handlePoisonCollision(cell1, cell2, toRemove);
            else {
                // To check if it is smaller than the other one.
                if (cell1.getRadius() == cell2.getRadius()) {
                    return;
                }
                if (cell1.getRadius() > cell2.getRadius()) {
                    toRemove.add(cell2);
                    growCell(cell1, cell2);
                    cell1.addScore(cell2);
                } else {
                    toRemove.add(cell1);
                    growCell(cell2, cell1);
                    cell2.addScore(cell1);
                }
            }
        }
    }

    private void handlePoisonCollision(Cell cell1, Cell cell2, ArrayList<Cell> toRemove) {
        Cell winner = null;
        Cell loser = null;

        // Both are poisonous, smaller one gets eaten.
        if (cell1.getIsPoisonous() && cell2.getIsPoisonous()) {
            if (cell1.getRadius() > cell2.getRadius()) {
                loser = cell2;
                winner = cell1;
            } else {
                loser = cell1;
                winner = cell2;
            }
        } else if (cell1.getIsPoisonous()) { // if cell1 is poisonous.
            // if cell2 is shielded and bigger than cell1.
            if (cell2.getIsShielded() && cell2.getRadius() > cell1.getRadius()) {
                loser = cell1;
                winner = cell2;
            } else { // otherwise cell2.
                loser = cell2;
                winner = cell1;
            }
        } else if (cell2.getIsPoisonous()) { // if cell2 is poisonous.
            // if cell1 is shielded and has bigger radius then cell2 dies.
            if (cell1.getIsShielded() && cell1.getRadius() > cell2.getRadius()) {
                loser = cell2;
                winner = cell1;
            } else { // cell1 dies here.
                loser = cell1;
                winner = cell2;
            }
        }

        // nothing happened.
        if (winner == null || loser == null)
            return;

        toRemove.add(loser);
        growCell(winner, loser);
        winner.addScore(loser);
    }

    private void handleSpikeCollision(Cell cell1, Cell cell2, ArrayList<Cell> toRemove) {
        // Both have spikes. We remove their spikes.
        if (cell1.getIsSpiked() && cell2.getIsSpiked()) {
            removeSpikes(cell1);
            removeSpikes(cell2);
        } else if (cell1.getIsSpiked()) { // we remove cell2
            // need to check if the other has shield. if yes then lose spike and shield.
            if (cell2.getIsShielded()) { // nothing happens
                removeSpikes(cell1);
                cell2.setIsShielded(false);
            } else {
                detectCellCollisionWithSpike(cell1, cell2, toRemove);
            }
        } else if (cell2.getIsSpiked()) { // we remove cell1
            // need to check if the other has shild if yes then lose spike and shield.
            if (cell1.getIsShielded()) { // nothing happens
                removeSpikes(cell2);
                cell1.setIsShielded(false);
            } else {
                detectCellCollisionWithSpike(cell2, cell1, toRemove);
            }
        }
    }

    private void removeSpikes(Cell cell) {
        cell.setIsSpiked(false);
        cell.resetDetectionRadius();
    }

    private void detectCellCollisionWithSpike(Cell cell1, Cell cell2, ArrayList<Cell> toRemove) {

        if (PVector.dist(cell1.getPosition(), cell2.getPosition()) <= (cell1.getDetectionRadius() + cell2.getDetectionRadius())) {
            toRemove.add(cell2);
            growCell(cell1, cell2);
            cell1.addScore(cell1);
        }
    }

    private void growCell(Cell eater, Cell eaten) {
        // adding the area of the cell and blob.
        float cellArea = eater.getRadius() * eater.getRadius() * p.PI;
        float eatenArea = eaten.getRadius() * eaten.getRadius() * p.PI;
        float newArea = cellArea + eatenArea;
        // getting the radius from the area.
        eater.setDiameter(sqrt(newArea / p.PI) * 2);
    }

    //********************************************************
    //              Cell + Shroom Collision
    //********************************************************

    public ArrayList<Cell> detectCellCollisionWithShrooms(ArrayList<Cell> cells) {
        ArrayList<Cell> toRemove = new ArrayList<>();
        for (Cell cell : cells) {
            // In case the cell is already removed.
            if (toRemove.contains(cell))
                continue;

            Iterator<Shroom> iterator = shrooms.iterator();
            while (iterator.hasNext()) {
                Shroom shroom = iterator.next();
                // In case it is the players own shroom or if the player is already removed.
                if (shroom.getOwner().getPlayerId() == cell.getPlayerId() || toRemove.contains(cell))
                    continue;

                // Check if they are colliding.
                if (PVector.dist(shroom.getPosition(), cell.getPosition()) < shroom.getRadius() + cell.getRadius()) {
                    // If the cell is not shielded then remove the player.
                    if (!cell.getIsShielded())
                        toRemove.add(cell);

                    // Remove the shroom after any contact.
                    iterator.remove();
                }

            }
        }

        cells.removeAll(toRemove);
        return toRemove;
    }

    //********************************************************
    //              Server Functionality.
    //********************************************************

    // These two methods basically simulate the player pressing on the buttons which can be used in our case.

    public void applyMouseAction(Cell cell, PlayerMessage playerMessage) {
        cell.integrate(new PVector(playerMessage.getMouseX(), playerMessage.getMouseY()));
    }

    public void applyKeyAction(Cell cell, PlayerMessage playerMessage) {
        if (playerMessage.getKeyPressed() == 'q' || playerMessage.getKeyPressed() == 'Q')
            cell.craftPoison();
        if (playerMessage.getKeyPressed() == 'w' || playerMessage.getKeyPressed() == 'W')
            cell.craftShield();
        if (playerMessage.getKeyPressed() == 'e' || playerMessage.getKeyPressed() == 'E')
            cell.craftSpike();
        if (playerMessage.getKeyPressed() == 'r' || playerMessage.getKeyPressed() == 'R')
            cell.craftRAbility();
    }

    //********************************************************
    //                      Blob + Material Generation
    //********************************************************
    public void generateBlobsAndMaterials() {
        generateBlobs();
        generateHerb();
        generateSteel();
        generateWood();
    }

    private void generateBlobs() {
        if (blobs.size() > 200)
            return;
        if (blobTimer == 0) {
            blobs.add(new Blob(p, p.random(-p.width, p.width), p.random(-p.height, p.height), 0, 0, 10, 10));
            blobTimer = 30;
        } else
            blobTimer--;
    }

    private void generateWood() {
        if (woodCount > 10)
            return;
        if (woodTimer == 0) {
            materials.add(new Wood(p, p.random(-p.width, p.width), p.random(-p.height, p.height), 0, 0, 20, 10));
            woodTimer = 40;
            woodCount++;
        } else
            woodTimer--;
    }

    private void generateSteel() {
        if (steelCount > 10)
            return;
        if (steelTimer == 0) {
            materials.add(new Steel(p, p.random(-p.width, p.width), p.random(-p.height, p.height), 0,
                    0, 20, 10));
            steelTimer = 40;
            steelCount++;
        } else
            steelTimer--;
    }

    private void generateHerb() {
        if (herbCount > 10)
            return;
        if (herbTimer == 0) {
            materials.add(new Herb(p, p.random(-p.width, p.width), p.random(-p.height, p.height), 0, 0, 20, 10));
            herbTimer = 40;
            herbCount++;
        } else
            herbTimer--;
    }

    //********************************************************
    //                      Getters
    //********************************************************
    public ArrayList<Blob> getBlobs() {
        return blobs;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public ArrayList<Shroom> getShrooms() {
        return shrooms;
    }
}
