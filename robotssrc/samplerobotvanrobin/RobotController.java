package samplerobotvanrobin;

import robocode.TeamRobot;

public abstract class RobotController {
    protected SMILE owner;
    public RobotController(SMILE owner) {
        this.owner = owner;
    }

    public abstract void update();
}
