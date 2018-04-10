package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to broadcast the location of a scanned target.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class SetTargetLocationMessage implements Serializable {
    private int x;
    private int y;

    public SetTargetLocationMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return this.y;
    }
}
