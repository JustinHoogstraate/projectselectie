package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to broadcast the target that is to be focused on by the target leaders.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class SetTargetMessage implements Serializable {
    private RobotReference target;

    public SetTargetMessage(RobotReference target) {
        this.target = target;
    }

    public RobotReference getTarget() {
        return target;
    }
}
