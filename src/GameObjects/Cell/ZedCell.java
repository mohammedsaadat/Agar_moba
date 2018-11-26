package GameObjects.Cell;

import processing.core.PApplet;

import java.io.Serializable;

public class ZedCell extends Cell implements Serializable {

    private final int ULTIMATE_COOLDOWN = 1000;
    private final int ULTIMATE_RUNNING_TIME = 50;
    private int ultimateRunningTime = -1;

    public ZedCell(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed, int playerId, String nickname) {
        super(p, x, y, xVel, yVel, diameter, speed, playerId, nickname);
    }

    public void integrateItemTimers() {
        super.integrateItemTimers();
        if (ultimateRunningTime > 0) {
            ultimateRunningTime--;
        } else if (ultimateRunningTime == 0) {

            // reduce the speed by half.
            speed = speed / 2f;
            ultimateRunningTime = -1;
        }
    }

    public void craftRAbility() {
        if (ultimateTimer != 0)
            return;

        // Gives the player twice the current speed for couple of seconds.
        speed = 2f * speed;
        ultimateRunningTime = ULTIMATE_RUNNING_TIME;
        ultimateTimer = ULTIMATE_COOLDOWN;
    }

    public int getUltimateCoolDown() {
        return ULTIMATE_COOLDOWN;
    }
}
