package samplerobotvanrobin;

import hybridattack.Generic.Vector2d;

public class EnemyReference extends RobotReference {
    public EnemyReference(String name, Vector2d location, double velocity, double heading) {
        super(name, location, velocity, heading,false);
    }
}
