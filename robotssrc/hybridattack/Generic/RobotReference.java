package hybridattack.Generic;

import java.io.Serializable;

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

    /**
     * Returns the angle from the given location to this robot, in degrees. Angle is absolute; 0 = global up, 90 = global right, and so on.
     * @param fromLocation The location from which to determine the angle to this robot.
     * @return The angle from the given location to this robot. Value is between 0 (inclusive) and 360 (exclusive).
     */
    public double getBearingTo(Vector2d fromLocation) {
        Vector2d relativeLocation = this.location.subtract(fromLocation);
        double angle = relativeLocation.getWorldBearing();
        while (angle >= 360) {
            angle -= 360;
        }
        return angle;
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
