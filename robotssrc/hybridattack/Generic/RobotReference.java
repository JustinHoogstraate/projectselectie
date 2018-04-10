package hybridattack.Generic;

import java.io.Serializable;

/**
 * This reference is used to broadcast a specific target, and all its available information.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class RobotReference implements Serializable {
    private Vector2d location;
    private String name;
    private Vector2d velocity;
    private double energy;
    private boolean isTeammate;
    private double heading;

    public RobotReference(String name, boolean isTeammate, Vector2d location, Vector2d velocity, double energy, double heading) {
        this.name = name;
        this.location = location;
        this.velocity = velocity;
        this.energy = energy;
        this.isTeammate = isTeammate;
        this.heading = heading;
    }

    /**
     * Get the world location of this robot. [0, 0] = the bottom left corner of the arena.
     * @return
     */
    public Vector2d getLocation() {
        return this.location;
    }

    /**
     * Returns the 2D velocity vector of this robot. To get the absolute velocity, call getLength() on the result vector.
     * @return
     */
    public Vector2d getVelocity() {
        return this.velocity;
    }

    /**
     * Get the unique name identifier of this robot.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the energy of this robot.
     * @return
     */
    public double getEnergy() {
        return this.energy;
    }

    /**
     * Sets the location of this robot.
     * @param value the Vector2D value the location is to be set to.
     */
    public void setLocation(Vector2d value) {
        this.location = value;
    }

    /**
     * Sets the velocity of this robot.
     * @param value the Vector2D value the velocity is to be set to.
     */
    public void setVelocity(Vector2d value) {
        this.velocity = value;
    }

    /**
     * Sets the energy of this robot.
     * @param value the Vector2D value the energy is to be set to.
     */
    public void setEnergy(double value) {
        this.energy = value;
    }

    /**
     * Checks if this Robot is a teammate.
     * @return false if it is an enemy, and true if it is a friendly.
     */
    public boolean isTeammate() {
        return this.isTeammate;
    }

    /**
     * Returns the heading (body facing direction) of this robot.
     * @return heading of the robot
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Sets the heading of this robot.
     * @param heading the heading that the heading of this robot is to be set to.
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * This method checks if the recieved RobotReference is about the same robot as this RobotReference.
     * @param obj the recieved RobotReference.
     * @return true is it's the same, false if it's a different robot.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RobotReference) {
            return ((RobotReference) obj).getName().equals(this.name);
        } else {
            return super.equals(obj);
        }
    }
}
