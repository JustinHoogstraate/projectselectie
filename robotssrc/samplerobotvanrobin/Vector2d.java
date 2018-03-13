package samplerobotvanrobin;

import java.io.Serializable;

public class Vector2d implements Serializable {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double value) {
        x = value;
    }

    public void setY(double value) {
        y = value;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns TRUE if the given vector is closer than the given distance; FALSE otherwise.
     *
     * @param other
     * @param maxDistance
     * @return
     */
    public boolean closerThan(Vector2d other, double maxDistance) {
        Vector2d relativeLocation = this.subtract(other);
        return relativeLocation.vectorLength() <= maxDistance;
    }

    /**
     * Returns a new vector that is the result of subtracting the given vector from this vector.
     *
     * @param other
     * @return
     */
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    /**
     * Returns a new vector that is the result of adding the given vector to this vector.
     *
     * @param other
     * @return
     */
    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d multiply(double factor) {
        return new Vector2d(
                this.x * factor,
                this.y * factor
        );
    }

    /**
     * Returns the length of this vector using Pythagoras' equation.
     *
     * @return
     */
    public double vectorLength() {
        return Math.sqrt((x * x) + (y * y));
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public static Vector2d getFromBearingAndDistance(double bearing, double distance) {
        double x = 0;
        double y = 0;

        x = Math.sin(Math.toRadians(bearing)) * distance;
        y = Math.cos(Math.toRadians(bearing)) * distance;

        return new Vector2d(x, y);
    }

    public double getWorldBearing() {
        double result = (Math.toDegrees(Math.atan2(y, x)) * -1) - 90;
        if (result < 0) {
            result += 360;
        }
        return result;
    }

    public Vector2d rotate(double angle) {
        double length = vectorLength();
        double bearing = getWorldBearing();
        bearing += angle;
        return Vector2d.getFromBearingAndDistance(bearing, length);
    }
}
