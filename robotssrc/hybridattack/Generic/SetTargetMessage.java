package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to broadcast the target that is to be focused on by the target leaders.
 *
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class SetTargetMessage implements Serializable {
    private RobotReference target;

    /**
     * This constructor is used to create the SetTargetMessage.
     *
     * @param target the Target that is used to create the SetTargetMessage.
     * @author Robin van Alst, Justin Hoogstraate.
     */
    public SetTargetMessage(RobotReference target) {
        this.target = target;
    }

    /**
     * This method is used to get the target from the SetTargetMessage.
     *
     * @return the target from the SetTargetMessage.
     * @author Robin van Alst, Justin Hoogstraate.
     */
    public RobotReference getTarget() {
        return target;
    }
}
