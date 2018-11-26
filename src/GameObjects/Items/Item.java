package GameObjects.Items;

import java.io.Serializable;

/*
The items that can be generated once the player picks up the resources(materials)
 */
public class Item implements Serializable {
    // Timer used to track how long they have been activated for.
    private int timer;

    Item(int timer) {
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }

    public void decrementTimer() {
        timer--;
    }
}
