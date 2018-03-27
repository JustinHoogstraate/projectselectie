package hybridattack.Generic;

import java.io.Serializable;

public class SetTargetMessage implements Serializable {
    private RobotReference target;

    public SetTargetMessage(RobotReference target) {
        this.target = target;
    }

    public RobotReference getTarget() {
        return target;
    }
}
