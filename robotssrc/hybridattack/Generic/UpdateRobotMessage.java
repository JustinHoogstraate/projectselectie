package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to update an existing RobotReference.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class UpdateRobotMessage implements Serializable {
    RobotReference reference;

    public UpdateRobotMessage(RobotReference reference) {
        this.reference = reference;
    }
}
