package hybridattack.Generic;

public class RobotReference {
    private Vector2d location;
    private String name;
    private Vector2d velocity;
    private double energy;

    public Vector2d getLocation() {
        return this.location;
    }

    public Vector2d getVelocity() {
        return this.velocity;
    }

    public String getName() {
        return this.name;
    }

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

    public void setVelocityFromBearingDistance(double bearing, double distance) {
        this.velocity = Vector2d.getFromBearingAndDistance(bearing, distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RobotReference) {
            return ((RobotReference)obj).getName().equals(this.name);
        }
        else {
            return super.equals(obj);
        }
    }
}