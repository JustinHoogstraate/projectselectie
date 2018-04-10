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

    public void setLocation(Vector2d value) {
        this.location = value;
    }

    public void setLocation(double x, double y) {
        setLocation(new Vector2d(x, y));
    }

    public void setVelocity(Vector2d value) {
        this.velocity = value;
    }

    public void setVelocity(double x, double y) {
        setVelocity(new Vector2d(x, y));
    }

    public void setEnergy(double value) {
        this.energy = value;
    }

    public boolean isTeammate() {
        return this.isTeammate;
    }

    /**
     * Returns the heading (body facing direction) of this robot.
     * @return
     */
    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RobotReference) {
            return ((RobotReference) obj).getName().equals(this.name);
        } else {
            return super.equals(obj);
        }
    }
}
