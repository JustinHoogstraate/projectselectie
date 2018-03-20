package hybridattack.Generic;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.util.HashMap;

public abstract class HybridAttackBase extends TeamRobot {
    protected Vector2d location;

    protected HashMap<String, RobotReference> robots = new HashMap();

    public HybridAttackBase() {
        updateVelocity();
    }

    @Override
    public void run() {
        super.run();
        updateVelocity();
    }

    private void updateVelocity() {
        double x = getX();
        double y = getY();
        location = new Vector2d(x, y);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);

        double bearing = event.getBearing();
        double distance = event.getDistance();
        Vector2d relativeLocation = Vector2d.getFromBearingAndDistance(bearing, distance);
        Vector2d absoluteLocation = relativeLocation.add(location);

        String name = event.getName();

        if (robots.containsKey(name)) {
            RobotReference robot = robots.get(name);
            robot.setLocation(absoluteLocation);
        }
        else {

        }
        double energy = event.getEnergy();
    }
}
