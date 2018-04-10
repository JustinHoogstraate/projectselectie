package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to update an existing RobotReference.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class UpdateRobotMessage implements Serializable {
    RobotReference reference;

    /**
     * This constructor is used to create an UpdateRobotMessage
     * @param reference the reference to the robot that is to be updated.
     * @author
     */
    public UpdateRobotMessage(RobotReference reference) {
        this.reference = reference;
    }
}
