package hybridattack.Generic;

import java.io.Serializable;

public class UpdateRobotMessage implements Serializable {
    RobotReference reference;

    public UpdateRobotMessage(RobotReference reference) {
        this.reference = reference;
    }
}
