package GameObjects;

import GameObjects.Cell.RivenCell;
import processing.core.PApplet;

import java.io.Serializable;

/*
The shrooms that generate from RIVEN CELL ultimate.
 */
public class Shroom extends GameObject implements Serializable {

    private RivenCell owner;

    public Shroom(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed, RivenCell owner) {
        super(p, x, y, xVel, yVel, diameter, speed);
        this.owner = owner;
    }

    // Returns the cell that generated this shroom.
    public RivenCell getOwner() {
        return owner;
    }
}
