package hybridattack.Generic;

import java.io.Serializable;

public class BulletReference implements Serializable {
    private double speed;
    private double velocity;
    private Vector2d position;

    public BulletReference(double speed, double velocity, Vector2d position) {
        this.speed = speed;
        this.velocity = velocity;
        this.position = position;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }
}
