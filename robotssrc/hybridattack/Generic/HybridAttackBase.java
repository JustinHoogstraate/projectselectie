package hybridattack.Generic;

import robocode.MessageEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.exception.RobotException;

import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public abstract class HybridAttackBase extends TeamRobot {
    protected Vector2d location;

    protected HashMap<String, RobotReference> robots = new HashMap();
    protected RobotReference teamTarget = null;

    private HashMap<String, Double> previousEnergyMap = new HashMap<>();

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

        for(String robot : robots.keySet()) {
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
        }

        if(enemyHasFired(steve)){
            //broadcast message
            EnemyFiredMessage message = new EnemyFiredMessage(steve.getLocation());
            try {
                broadcastMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        Serializable message = event.getMessage();

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

    private void turnToVector(Vector2d vector){
        Vector2d relativeLocation = location.subtract(vector);
        double angle = relativeLocation.getWorldBearing();
        double localHeading = angle - getGunHeading();
        if (localHeading > 180) {
            localHeading -= 360;
        }
        if (localHeading > 0) {
            turnGunRight(localHeading);
        } else if (localHeading < 0) {
            turnGunLeft(localHeading * -1);
        }
    }


}
