package hybridattack.Generic;

import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.exception.RobotException;

import java.util.HashMap;

public abstract class HybridAttackBase extends TeamRobot {
    protected Vector2d location;

    protected HashMap<String, RobotReference> robots = new HashMap();
    protected RobotReference teamTarget = null;

    public HybridAttackBase() {

    }

    @Override
    public void run() {
        super.run();

        setTurnRadarRight(360);

        updateLocation();

        while(getRadarTurnRemaining() != 0 || getDistanceRemaining() != 0 || getGunTurnRemaining() != 0 || getTurnRemaining() != 0) {
            execute();
        }
    }

    private void updateLocation() {
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

        double heading = event.getHeading();
        double velocity = event.getVelocity();
        Vector2d velocity2d = Vector2d.getFromBearingAndDistance(heading, velocity);

        String name = event.getName();
        boolean isTeammate = isTeammate(name);
        double energy = event.getEnergy();

        if (robots.containsKey(name)) {
            RobotReference robot = robots.get(name);
            robot.setLocation(absoluteLocation);
            robot.setVelocity(velocity2d);
            robot.setEnergy(energy);
        } else {
            RobotReference steve = new RobotReference(name, isTeammate, absoluteLocation, velocity2d, energy);
            robots.put(name, steve);
        }
    }
}
