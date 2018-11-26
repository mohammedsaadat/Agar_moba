package GameObjects.Cell;

import processing.core.PApplet;

import java.io.Serializable;

public class JaxCell extends Cell implements Serializable {

    private final int ULTIMATE_COOLDOWN = 2000;

    public JaxCell(PApplet p, float x, float y, float xVel, float yVel, float diameter, float speed, int playerId, String nickname) {
        super(p, x, y, xVel, yVel, diameter, speed, playerId, nickname);
    }


    public void craftRAbility() {
        if (ultimateTimer != 0)
            return;
        /*
         For Jax, he will get enough resources to use the Q, W and E abilities.
        */
        // Add enough for Poison.
        inventory.incrementHerb(2);
        // Add enough for Shield.
        inventory.incrementWood(2);
        inventory.incrementSteel(1);
        // Add enough for Spike
        inventory.incrementWood(3);
        inventory.incrementSteel(2);
        inventory.incrementHerb(1);

        ultimateTimer = ULTIMATE_COOLDOWN;
    }

    public int getUltimateCoolDown() {
        return ULTIMATE_COOLDOWN;
    }
}
