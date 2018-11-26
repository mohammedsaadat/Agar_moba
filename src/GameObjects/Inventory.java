package GameObjects;

import GameObjects.Cell.Cell;
import GameObjects.Items.Item;
import GameObjects.Items.Poison;
import GameObjects.Items.Shield;
import GameObjects.Items.Spike;

import java.io.Serializable;

public class Inventory implements Serializable {

    private Item[] slots;

    // The running time of the crafted items.
    public static final int POISON_TIMER = 50;
    public static final int SHIELD_TIMER = 50;
    public static final int SPIKE_TIMER = 50;


    public Inventory() {
        slots = new Item[4];
    }

    private int herbCount = 0;
    private int woodCount = 0;
    private int steelCount = 0;

    public void incrementHerb(int value) {
        herbCount = herbCount + value;
    }

    public void incrementWood(int value) {
        woodCount = woodCount + value;
    }

    public void incrementSteel(int value) {
        steelCount = steelCount + value;
    }

    // Puts poison in the 0 slot of the inventory.
    public boolean craftPoison() {
        if (checkPoisonReq()) {
            slots[0] = new Poison(POISON_TIMER);
            // returns true if it was possible.
            return true;
        }
        return false;
    }

    // Puts shield in the 1 slot of the inventory.
    public boolean craftShield() {
        if (checkShieldReq()) {
            slots[1] = new Shield(SHIELD_TIMER);
            return true;
        }
        return false;
    }

    // Puts poison in the 2 slot of the inventory.
    public boolean craftSpike(Cell cell) {
        if (checkSpikeReq()) {
            slots[2] = new Spike(SPIKE_TIMER);
            // increases the diameter of the radius.
            cell.incrementDetectionDiameter(cell.getRadius() / 2);
            return true;
        }
        return false;
    }

    private boolean checkPoisonReq() {
        if (herbCount >= 2) {
            herbCount -= 2;
            return true;
        }
        return false;
    }

    private boolean checkShieldReq() {
        if (woodCount >= 2 && steelCount >= 1) {
            woodCount -= 2;
            steelCount -= 1;
            return true;
        }
        return false;
    }

    private boolean checkSpikeReq() {
        if (woodCount >= 3 && steelCount >= 2 && herbCount >= 1) {
            woodCount -= 3;
            steelCount -= 2;
            herbCount -= 1;
            return true;
        }
        return false;
    }

    public int getHerbCount() {
        return herbCount;
    }

    public Item getPoison() {
        return slots[0];
    }

    public Item getShield() {
        return slots[1];
    }

    public Item getSpikes() {
        return slots[2];
    }

    public int getWoodCount() {
        return woodCount;
    }

    public int getSteelCount() {
        return steelCount;
    }

}
