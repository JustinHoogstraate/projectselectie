package hybridattack.Generic;

public class RobotReference {
    private Vector2d location;
    private String name;
    private Vector2d velocity;
    private double energy;
    private boolean isTeammate;

    public RobotReference(String name, boolean isTeammate, Vector2d location, Vector2d velocity, double energy) {
        this.name = name;
        this.location = location;
        this.velocity = velocity;
        this.energy = energy;
        this.isTeammate = isTeammate;
    }

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

    public void setEnergy(double value) {
        this.energy = value;
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
