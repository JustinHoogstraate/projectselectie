package samplerobotvanrobin;

import java.io.Serializable;

public class FocusOnEnemyMessage implements Serializable {
    private EnemyReference target;

    public FocusOnEnemyMessage(EnemyReference target) {
        this.target = target;
    }

    public EnemyReference getTarget() {
        return target;
    }
}
