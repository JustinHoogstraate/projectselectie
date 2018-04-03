package hybridattack.Generic;

import java.io.Serializable;

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
