package samplerobotvanrobin;

public class RobotReference {
    private boolean isTeammate = false;
    private String name;
    private Vector2d location;
    private double velocity;
    private double heading;

    public double getVelocity() {
        return velocity;
    }

    public double getHeading() {
        return heading;
    }

    public Vector2d getVelocityVector() {
        return Vector2d.getFromBearingAndDistance(heading, velocity);
    }

    public RobotReference(String name, Vector2d location, double velocity, double heading, boolean isTeammate) {
        this.name = name;
        this.location = location;
        this.velocity = velocity;
        this.heading = heading;
        this.isTeammate = isTeammate;
    }

    public String getName() {
        return name;
    }

    public boolean isTeammate() {
        return this.isTeammate;
    }

    public Vector2d getWorldLocation() {
        return location;
    }

    public boolean equals(RobotReference other) {
        return (other.name == name);
    }

    public void update(Vector2d location, double velocity, double heading) {
        this.location = location;
        this.velocity = velocity;
        this.heading = heading;
    }
}
