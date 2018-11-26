package GameObjects.Cell;

import GameObjects.Shroom;
import processing.core.PApplet;

import java.io.Serializable;
import java.util.ArrayList;

public class RivenCell extends Cell implements Serializable {

    private final int ULTIMATE_COOLDOWN = 2000;
    ArrayList<Shroom> shrooms;

    public RivenCell(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed,
                     int playerId, String nickname, ArrayList<Shroom> shrooms) {

        super(p, x, y, xVel, yVel, diameter, speed, playerId, nickname);
        this.shrooms = shrooms;
    }

    public void craftRAbility() {
        if (ultimateTimer != 0)
            return;

        // There will be a shroom generated in the centre of the cell itself.
        shrooms.add(new Shroom(p, position.x, position.y, 0, 0, 20, 0, this));

        ultimateTimer = ULTIMATE_COOLDOWN;
    }

    public int getUltimateCoolDown() {
        return ULTIMATE_COOLDOWN;
    }
}
