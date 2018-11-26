package GameObjects;

import processing.core.PApplet;

import java.io.Serializable;

/*
Represents the food blobs that the cell can pick up to grow.
 */
public class Blob extends GameObject implements Serializable {
    public Blob(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed) {
        super(p, x, y, xVel, yVel, diameter, speed);
    }
}
