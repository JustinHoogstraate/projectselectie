package samplerobotvanrobin;

import java.util.ArrayList;

public class RobotManager {
    private ArrayList<RobotReference> robots = new ArrayList();

    public RobotReference getRobotByName(String name) {
        for(RobotReference ref : robots) {
            if (ref.getName().equals(name)) {
                return ref;
            }
        }
        return null;
    }

    public TeammateReference getTeammate(String name) {
        RobotReference robot = getRobotByName(name);
        if (robot != null && robot.isTeammate()) {
            return (TeammateReference)robot;
        }
        else {
            return null;
        }
    }

    public EnemyReference getEnemy(String name) {
        RobotReference robot = getRobotByName(name);
        if (robot != null && !robot.isTeammate()) {
            return (EnemyReference)robot;
        }
        else {
            return null;
        }
    }

    public void addTeammate(String name, Vector2d position, double velocity, double heading) {
        if (getTeammate(name) == null) {
            TeammateReference teammate = new TeammateReference(name, position, velocity, heading);
            robots.add(teammate);
        }
    }

    public void addEnemy(String name, Vector2d position, double velocity, double heading) {
        if (getEnemy(name) == null) {
            EnemyReference enemy = new EnemyReference(name, position, velocity, heading);
            robots.add(enemy);
        }
    }

    public boolean hasRobot(String name) {
        return getRobotByName(name) != null;
    }

    public void addRobot(RobotReference existing) {
        if (getRobotByName(existing.getName()) == null) {
            robots.add(existing);
        }
    }

    public void updateRobot(String name, Vector2d location, double velocity, double heading) {
        RobotReference robot = getRobotByName(name);
        if (robot != null) {
            robot.update(location, velocity, heading);
        }
    }

    public RobotReference getClosestRobot(Vector2d location) {
        RobotReference result = null;

        double closestDistance = 10000.0;
        for(RobotReference robot : robots) {
            Vector2d robotLocation = robot.getWorldLocation();
            Vector2d delta = location.subtract(location);
            if (delta.vectorLength() < closestDistance) {
                result = robot;
                closestDistance = delta.vectorLength();
            }

        }

        return result;
    }
}
