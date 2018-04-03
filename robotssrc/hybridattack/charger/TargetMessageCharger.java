package hybridattack.charger;

import hybridattack.Generic.RobotReference;

import java.io.Serializable;

public class TargetMessageCharger implements Serializable {
    private RobotReference target;

    public TargetMessageCharger(RobotReference target) {
        this.target = target;
    }

    public RobotReference getTarget() {
        return target;
    }
}
