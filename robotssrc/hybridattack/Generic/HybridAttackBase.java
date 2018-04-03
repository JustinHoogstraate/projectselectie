package hybridattack.Generic;

import robocode.*;
import robocode.exception.RobotException;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public abstract class HybridAttackBase extends TeamRobot {
    protected Vector2d location;

    protected HashMap<String, RobotReference> robots = new HashMap();
    protected RobotReference teamTarget = null;

    private HashMap<String, Double> previousEnergyMap = new HashMap<>();

    protected final int DISTANCE_FROM_WALLS = 100;

    protected boolean forward = false;

    public HybridAttackBase() {

    }

    protected ArrayList<RobotReference> getEnemies() {
        return getRobotsByAllegiance(false);
    }

    protected ArrayList<RobotReference> getTeam() {
        return getRobotsByAllegiance(true);
    }

    private ArrayList<RobotReference> getRobotsByAllegiance(boolean teammate) {
        ArrayList<RobotReference> result = new ArrayList();

        for (String robot : robots.keySet()) {
            if (robots.get(robot).isTeammate() == teammate) {
                result.add(robots.get(robot));
            }
        }

        return result;
    }

    @Override
    public void run() {
        super.run();

        setTurnRadarRight(360);

        updateLocation();

        forward = getVelocity() > 0;

        if (isNearTopWall()) {
            onNearWall(0);
        }
        if (isNearRightWall()) {
            onNearWall(1);
        }
        if (isNearBottomWall()) {
            onNearWall(2);
        }
        if (isNearLeftWall()) {
            onNearWall(3);
        }

        while (getRadarTurnRemaining() != 0 || getDistanceRemaining() != 0 || getGunTurnRemaining() != 0 || getTurnRemaining() != 0) {
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

        double bearing = event.getBearing() + getHeading();
        double distance = event.getDistance();
        Vector2d relativeLocation = Vector2d.getFromBearingAndDistance(bearing, distance);
        Vector2d absoluteLocation = relativeLocation.add(location);

        double heading = event.getHeading();
        double velocity = event.getVelocity();
        Vector2d velocity2d = Vector2d.getFromBearingAndDistance(heading, velocity);

        String name = event.getName();
        boolean isTeammate = isTeammate(name);
        double energy = event.getEnergy();

        RobotReference steve;

        if (robots.containsKey(name)) {
            steve = robots.get(name);
            steve.setLocation(absoluteLocation);
            steve.setVelocity(velocity2d);
            steve.setEnergy(energy);
            steve.setHeading(heading);
        } else {
            steve = new RobotReference(name, isTeammate, absoluteLocation, velocity2d, energy, heading);
            robots.put(name, steve);
        }
        try {
            broadcastMessage(new UpdateRobotMessage(steve));
        } catch (IOException ioe) {
             //ignore
        }

        if (!steve.isTeammate()) {
            if (teamTarget == null && teamTarget != steve) {
                setTeamTarget(steve);
            }

            if (!steve.isTeammate()) {
                if (enemyHasFired(steve)) {
                    onEnemyFired(steve.getLocation());
                }
                previousEnergyMap.put(steve.getName(), steve.getEnergy());
            }

        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        Serializable message = event.getMessage();
        if (message instanceof SetTargetMessage) {
            RobotReference target = ((SetTargetMessage) message).getTarget();
            setTeamTarget(target);
        }
    }

    private boolean enemyHasFired(RobotReference robotReference) {
        try {
            if (robotReference.getEnergy() < previousEnergyMap.get(robotReference.getName())) {
                return true;
            }
        } catch (NullPointerException exception) {
            //Do nothing
            //There is no previous energy data
        }
        return false;
    }

    protected void turnToVector(Vector2d vector) {
        Vector2d relativeLocation = location.subtract(vector);
        double angle = relativeLocation.getWorldBearing();
        double localHeading = angle - getHeading();
        if (localHeading > 180) {
            localHeading -= 360;
        }
        if (localHeading > 0) {
            setTurnRight(localHeading);
        } else if (localHeading < 0) {
            setTurnLeft(localHeading * -1);
        }
    }

    protected void pointGunToVector(Vector2d vector){
        Vector2d relativeLocation = location.subtract(vector);
        double angle = relativeLocation.getWorldBearing();
        double localHeading = angle - getGunHeading();
        if (localHeading > 180) {
            localHeading -= 360;
        }
        if (localHeading > 0) {
            turnGunRight(localHeading);
        } else if (localHeading < 0) {
            turnGunRight(localHeading * -1);
        }
    }

    protected void onEnemyFired(Vector2d location) {
        //broadcast message
        EnemyFiredMessage message = new EnemyFiredMessage(location);
        try {
            broadcastMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Vector2d getLocation() {
        return new Vector2d(getX(), getY());
    }

    private void onNearWall(int wall) {
        switch (wall) {
            case 0:
                if (headingInRange(270, 90, forward)) {
                    reverse();
                }
                break;
            case 1:
                if (headingInRange(0, 180, forward)) {
                    reverse();
                }
                break;
            case 2:
                if (headingInRange(90, 270, forward)) {
                    reverse();
                }
                break;
            case 3:
                if (headingInRange(180, 360, forward)) {
                    reverse();
                }
                break;
        }
    }

    private boolean headingInRange(int from, int to, boolean forward) {
        if (!forward) {
            from += 180;
            while (from > 360) {
                from -= 360;
            }
            to += 180;
            while (to > 360) {
                to -= 360;
            }
        }
        if (to < from) {
            if (getHeading() >= from || getHeading() <= to) {
                return true;
            }
        }
        else {
            if (getHeading() >= from && getHeading() <= to) {
                return true;
            }
        }
        return false;
    }

    protected void reverse() {
        System.out.println("reversing");
    }

    protected boolean isNearLeftWall() {
        return getX() <= DISTANCE_FROM_WALLS;
    }

    protected boolean isNearRightWall() {
        return getX() >= getBattleFieldWidth() - DISTANCE_FROM_WALLS;
    }

    protected boolean isNearTopWall() {
        return getY() >= getBattleFieldHeight() - DISTANCE_FROM_WALLS;
    }

    protected boolean isNearBottomWall() {
        return getX() <= getBattleFieldHeight() - DISTANCE_FROM_WALLS;
    }

    protected void setTeamTarget(RobotReference reference) {
        this.teamTarget = reference;
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);
        String name = event.getName();
        if (teamTarget.getName().equals(name)) {
            teamTarget = null;
        }
        robots.remove(name);
    }
}
