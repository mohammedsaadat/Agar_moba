package GameObjects.Cell;

import GameObjects.GameObject;
import GameObjects.Inventory;
import GameObjects.Materials.Herb;
import GameObjects.Materials.Material;
import GameObjects.Materials.Steel;
import GameObjects.Materials.Wood;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;


/*
This class represents the cell that each player will control.
 */
public class Cell extends GameObject implements Serializable, Comparable<Cell> {

    protected int playerId;
    private int eatenBlobs;
    protected String nickname;

    // Boundary Range Variables
    private final float CHANGE_ANGLE = 20;

    Inventory inventory;
    private boolean isPoisonous = false;
    private boolean isShielded = false;
    private boolean isSpiked = false;
    private float detectionDiameter;

    int ultimateTimer = 0;

    public Cell(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed, int playerId, String nickname) {
        super(p, x, y, xVel, yVel, diameter, speed);
        inventory = new Inventory();
        detectionDiameter = diameter;
        this.playerId = playerId;
        this.nickname = nickname;
    }

    public void integrate(PVector mouse) {


        // detection diameter changes as when we have spikes the radius should increase.
        detectionDiameter = diameter;

        // decrement the items + ultimate timers.
        integrateItemTimers();

        // to centre of the sketch.
        PVector target = new PVector(mouse.x - p.width / 2, mouse.y - p.height / 2);

        float targetMag = target.mag();

        // if within 5 then stop.
        if (targetMag < 5) {
            return;
        }

        // Makes sure it does not leave the sides.
        keepInBoundary(target);

        if (targetMag > speed) {
            target.normalize();
            target.mult(speed);
            position.add(target);
        }
    }

    //keeps player in screen.
    private void keepInBoundary(PVector target) {
        float radius = getRadius();
        // Left of the screen (first condition), Right of the screen (Second condition).
        if (position.x - radius < -p.width && target.x < 0 || position.x + radius > p.width && target.x > 0) {
            target.x = 0;
            // If within -20 to 20 then keep Y direction still as well.
            if (target.y <= CHANGE_ANGLE && target.y >= -CHANGE_ANGLE)
                target.y = 0;
        }

        // Top and Bottom of the screen
        if (position.y - radius < -p.height && target.y < 0 || position.y + radius > p.height && target.y > 0) {
            target.y = 0;
            // If within -20 to 20 then keep X direction still as well.
            if (target.x <= CHANGE_ANGLE && target.x >= -CHANGE_ANGLE)
                target.x = 0;
        }
    }

    public void incrementEatenBlobs() {
        eatenBlobs = eatenBlobs + 1;
    }

    // Reduces current speed.
    public void reduceSpeed() {
        this.speed = (speed < 0.7) ? speed : speed - (0.005f / getRadius()) * speed;
    }

    /*
    Checks the type of the material then increases the count of that material by one.
     */
    public void addToInventory(Material material) {
        if (material instanceof Herb)
            inventory.incrementHerb(1);
        if (material instanceof Wood)
            inventory.incrementWood(1);
        if (material instanceof Steel)
            inventory.incrementSteel(1);

    }

    // Getters.
    public int getEatenBlobs() {
        return eatenBlobs;
    }

    public boolean getIsPoisonous() {
        return isPoisonous;
    }

    public boolean getIsShielded() {
        return isShielded;
    }

    public boolean getIsSpiked() {
        return isSpiked;
    }

    public float getDetectionRadius() {
        return detectionDiameter / 2f;
    }

    public int getPoisonTimer() {
        return inventory.getPoison().getTimer();
    }

    public int getShieldTimer() {
        return inventory.getShield().getTimer();
    }

    public int getSpikeTimer() {
        return inventory.getSpikes().getTimer();
    }

    public int getUltimateTimer() {
        return ultimateTimer;
    }

    public int getUltimateCoolDown() {
        return 0;
    }

    public int getHerbCount() {
        return inventory.getHerbCount();
    }

    public int getWoodCount() {
        return inventory.getWoodCount();
    }

    public int getSteelCount() {
        return inventory.getSteelCount();
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayerId() {
        return playerId;
    }

    // If already on item is active the same item won't be activated.
    public void craftPoison() {
        if (isPoisonous) return;
        isPoisonous = inventory.craftPoison();
    }

    public void craftShield() {
        if (isShielded) return;
        isShielded = inventory.craftShield();
    }

    public void craftSpike() {
        if (isSpiked) return;
        isSpiked = inventory.craftSpike(this);
    }

    public void craftRAbility() {
    }

    // subtracts the items and ultimate/ability timers.
    public void integrateItemTimers() {
        if (isPoisonous) {
            inventory.getPoison().decrementTimer();
            if (inventory.getPoison().getTimer() == 0)
                isPoisonous = false;
        }

        if (isShielded) {
            inventory.getShield().decrementTimer();
            if (inventory.getShield().getTimer() == 0)
                isShielded = false;
        }

        if (isSpiked) {
            inventory.getSpikes().decrementTimer();
            if (inventory.getSpikes().getTimer() == 0) {
                isSpiked = false;
                resetDetectionRadius();
            }

        }

        if (ultimateTimer > 0) {
            ultimateTimer--;
        }
    }

    public void setIsSpiked(boolean value) {
        isSpiked = value;
    }

    public void setIsShielded(boolean value) {
        isShielded = value;
    }

    // Increases the detection diameter
    public void incrementDetectionDiameter(float increment) {
        detectionDiameter += increment;
    }

    public void resetDetectionRadius() {
        detectionDiameter = diameter;
    }

    // add the blobs of the eaten cell.
    public void addScore(Cell cell) {
        eatenBlobs = (eatenBlobs + cell.eatenBlobs);
    }

    // Used for the sorting in the GameDraw Class.
    @Override
    public int compareTo(Cell o) {
        return o.eatenBlobs - this.eatenBlobs;
    }
}
