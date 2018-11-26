package GameObjects;

import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;

public abstract class GameObject implements Serializable {
  /*
    This class represents all of the game objects in this game. It is adapted from my previous practical solution.
  */

    transient protected PApplet p;

    protected float speed;
    protected PVector position;
    private PVector velocity;
    protected float diameter;


    public GameObject(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed) {
        position = new PVector(x, y);
        velocity = new PVector(xVel, yVel);
        this.diameter = diameter;
        this.p = p;
        this.speed = speed;
    }

    public PVector getPosition() {
        return position;
    }

    public float getRadius() {
        return diameter / 2f;
    }

    public float getDiameter() {
        return diameter;
    }

    public PApplet getP() {
        return p;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

}
