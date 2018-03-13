package samplerobotvanrobin.Tests;

import robocode.AdvancedRobot;

public class MovingTarget extends AdvancedRobot {
    public void run() {
        int movementspeed = 1;
        while(true) {
            if (getX() < 50) {
                movementspeed = 1;
            } else if (getX() > getBattleFieldWidth() - 50) {
                movementspeed = -1;
            }
            double heading = getHeading();
            heading  = 90 - heading;
            if (heading > 0) {
                setTurnRight(heading);
            }
            else {
                setTurnLeft(Math.abs(heading));
            }
            execute();

            if (movementspeed > 0) {
                setAhead(100);
            }
            else {
                setBack(100);
            }
        }
    }
}
