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
import java.util.List;

public abstract class HybridAttackBase extends TeamRobot {
    protected final int TOP_WALL = 0;
    protected final int RIGHT_WALL = 1;
    protected final int BOTTOM_WALL = 2;
    protected final int LEFT_WALL = 3;
    protected final int NEAR_ROBOT = 4;

    protected Vector2d location;

    protected HashMap<String, RobotReference> robots = new HashMap();
    protected RobotReference teamTarget = null;

    private HashMap<String, Double> previousEnergyMap = new HashMap<>();

    protected final int DISTANCE_FROM_WALLS = 50;

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

        //while (getRadarTurnRemaining() != 0 || getDistanceRemaining() != 0 || getGunTurnRemaining() != 0 || getTurnRemaining() != 0) {
        execute();
        //}
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
        Vector2d relativeLocation = vector.subtract(location);
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

    protected void pointGunToVector(Vector2d vector) {
        setAdjustGunForRobotTurn(true);
        Vector2d relativeLocation = vector.subtract(location);
        double angle = relativeLocation.getWorldBearing();
        double localHeading = angle - getGunHeading();
        while (localHeading > 180) {
            localHeading -= 360;
        }
        if (localHeading > 0) {
            turnGunRight(localHeading);
        } else if (localHeading < 0) {
            turnGunLeft(Math.abs(localHeading));
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

    protected Vector2d getVelocity2d() {
        return Vector2d.getFromBearingAndDistance(getHeading(), 100);
    }

    protected void reverse() {
        System.out.println("reversing");
        forward = !forward;
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
        return getY() <= DISTANCE_FROM_WALLS;
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

    /**
     * Returns the walls that we are close to.
     *
     * @return
     */
    protected List<Integer> getNearbyObstacles() {
        List<Integer> result = new ArrayList();

        for (String ref : robots.keySet()) {
            if (isNearRobot(robots.get(ref))) {
                result.add(NEAR_ROBOT);
            }
        }

        if (isNearTopWall()) {
            result.add(TOP_WALL);
        }
        if (isNearRightWall()) {
            result.add(RIGHT_WALL);
        }
        if (isNearBottomWall()) {
            result.add(BOTTOM_WALL);
        }
        if (isNearLeftWall()) {
            result.add(LEFT_WALL);
        }


        return result;
    }

    protected boolean isNearRobot(RobotReference ref) {
        return ref.getLocation().subtract(getLocation()).vectorLength() < DISTANCE_FROM_WALLS;
    }
}
