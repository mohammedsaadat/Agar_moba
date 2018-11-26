package GameObjects.Cell;

import processing.core.PApplet;

import java.io.Serializable;

public class LuluCell extends Cell implements Serializable {

    private final int ULTIMATE_COOLDOWN = 1500;
    private final int ULTIMATE_RUNNING_TIME = 50;
    private int ultimateRunningTime = -1;

    public LuluCell(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed, int playerId, String nickname) {
        super(p, x, y, xVel, yVel, diameter, speed, playerId, nickname);
    }

    public void integrateItemTimers() {
        super.integrateItemTimers();

        if (ultimateRunningTime > 0) {
            ultimateRunningTime--;
        } else if (ultimateRunningTime == 0) {
            ultimateRunningTime = -1;
            /*
             Only remove the increase factor by the ultimate, whatever the cell eats will be added to its
             diameter once gone back to normal.
            */
            // reseting the diameter back.
            diameter = diameter - 0.5f * diameter;
        }
    }

    public void craftRAbility() {
        if (ultimateTimer != 0)
            return;

        // Increase the diameter by half of the original.
        diameter = diameter + 0.5f * diameter;

        ultimateRunningTime = ULTIMATE_RUNNING_TIME;
        ultimateTimer = ULTIMATE_COOLDOWN;
    }

    public int getUltimateCoolDown() {
        return ULTIMATE_COOLDOWN;
    }
}
