package hybridattack.Generic;

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

    protected HashMap<String, RobotReference> robots = new HashMap();
    protected RobotReference teamTarget = null;

    private HashMap<String, Double> previousEnergyMap = new HashMap<>();

    protected boolean forward = false;

    /**
     * Returns a list of enemy robots
     *
     * @return the list of enemy robots
     * @author Robin van Alst
     * */
    protected ArrayList<RobotReference> getEnemies() {
        return getRobotsByAllegiance(false);
    }

    /**
     * Returns a list of teammates
     *
     * @return the list of friendly robots
     * @author Robin van Alst
     * */
    protected ArrayList<RobotReference> getTeam() {
        return getRobotsByAllegiance(true);
    }

    /**
     * Returns a list of robots that are friendly or not
     *
     * @param teammate if the robots should be a teammate or an enemy
     * @return the list of robots
     * @author Robin van Alst
     * */
    private ArrayList<RobotReference> getRobotsByAllegiance(boolean teammate) {
        ArrayList<RobotReference> result = new ArrayList();

        for (String robot : robots.keySet()) {
            if (robots.get(robot).isTeammate() == teammate) {
                result.add(robots.get(robot));
            }
        }

        return result;
    }

    /**
     * The generic run method that should be executed on every robot
     *
     * @author Robin van Alst
     * */
    @Override
    public void run() {
        super.run();
        setTurnRadarRight(360);
        execute();
    }

    /**
     * The generic method that should be used by every robot. <br>
     * whenever a robot is scanned the robot reference to that robot will be created
     * if it doesn't already exists, otherwise the reference will be updated with the new information
     * and broadcasted to all teammates. <br>
     * When there is no teamtarget set and the scanned robot is not a teammate the robot will be set as the
     * team target
     *
     * @param event the given information by the event
     * @author Robin van Alst, Justin Hoogstraate
     * */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);

        double bearing = event.getBearing() + getHeading();
        double distance = event.getDistance();
        Vector2d relativeLocation = Vector2d.getFromBearingAndDistance(bearing, distance);
        Vector2d absoluteLocation = relativeLocation.add(getLocation());

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
            if (teamTarget == null) {
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

    /**
     * This method will be called whenever a message is received <br>
     * If the message is if the type SetTargetMessage the teamtarget will be set
     * to the new teamtarget
     *
     * @param event message that is received
     * */
    @Override
    public void onMessageReceived(MessageEvent event) {
        Serializable message = event.getMessage();
        if (message instanceof SetTargetMessage) {
            RobotReference target = ((SetTargetMessage) message).getTarget();
            setTeamTarget(target);
        }
    }

    /**
     * Checks if the enemy has fired
     *
     * @param robotReference the robot that will be checked
     * @return the boolean if the robot has fired or not
     * @author Justin Hoogstraate
     */
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

    /**
     * Turns the robot to a given location
     *
     * @param vector the location that the robot should point towards
     * @author Justin Hoogstraate
     * */
    protected void turnToVector(Vector2d vector) {
        Vector2d relativeLocation = vector.subtract(getLocation());
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

    /**
     * turns the gun to a given location
     *
     * @param vector the location that the gun should point towards
     * @author Justin Hoogstraate
     * */
    protected void pointGunToVector(Vector2d vector) {
        setAdjustGunForRobotTurn(true);
        Vector2d relativeLocation = vector.subtract(getLocation());
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


    /**
     * Returns a Vector2d with the x and y location of the robot
     *
     * @return the location of the robot
     * @author Robin van Alst
     * */
    protected Vector2d getLocation() {
        return new Vector2d(getX(), getY());
    }

    protected void setTeamTarget(RobotReference reference) {
        this.teamTarget = reference;
    }


    /**
     * This method is called whenever a robot dies
     * in this function the reference to this robot will be deleted
     * and if this robot was the team target, the team target will be set to null
     *
     * @author Justin Hoogstraate, Thomas Heinsbroek, Robin van Alst
     * */
    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);
        String name = event.getName();
        if(teamTarget != null) {
            if (teamTarget.getName().equals(name)) {
                teamTarget = null;
            }
        }
        if(robots.containsKey(name)) {
            robots.remove(name);

        }
    }
}
