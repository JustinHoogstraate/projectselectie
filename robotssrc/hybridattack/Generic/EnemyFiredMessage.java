package hybridattack.Generic;

import java.io.Serializable;

public class EnemyFiredMessage implements Serializable {
    private Vector2d firedFromLocation;

    public EnemyFiredMessage(Vector2d firedFromLocation) {
        this.firedFromLocation = firedFromLocation;
    }
}
