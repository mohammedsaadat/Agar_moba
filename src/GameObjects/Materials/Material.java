package GameObjects.Materials;

import GameObjects.GameObject;
import processing.core.PApplet;


/*
The parent class that represents the materials that the cell can pick up in order to craft items.
 */
public class Material extends GameObject {

    Material(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed) {
        super(p, x, y, xVel, yVel, diameter, speed);
    }


}
