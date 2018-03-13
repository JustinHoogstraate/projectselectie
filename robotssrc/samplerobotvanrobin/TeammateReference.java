package samplerobotvanrobin;

public class TeammateReference extends RobotReference {
    public TeammateReference(String name, Vector2d location, double velocity, double heading) {
        super(name, location,velocity, heading, true);
    }
}
