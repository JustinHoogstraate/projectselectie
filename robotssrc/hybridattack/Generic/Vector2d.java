package hybridattack.Generic;

import java.io.Serializable;

/**
 * This class is used to accurately define location by implementing 2D vectors.
 *
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class Vector2d implements Serializable {
    private double x;
    private double y;

    /**
     * The constructor used to create the Vector2d.
     *
     * @param x the x value that is used to create the Vector2d.
     * @param y the y value that is used to create the Vector2d.
     * @author Robin van Alst.
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method is used to get the X value of the Vector2d.
     *
     * @return the x of the Vector2d.
     * @author Robin van Alst.
     */
    public double getX() {
        return x;
    }

    /**
     * This method is used to get the Y value of the Vector2d.
     *
     * @return the y of the Vector2d.
     * @author Robin van Alst.
     */
    public double getY() {
        return y;
    }

    /**
     * This method is used to set the X value of the Vector2d.
     *
     * @param value the value the X is to be set to.
     * @author Robin van Alst.
     */
    public void setX(double value) {
        x = value;
    }

    /**
     * This method is used to set the Y value of the Vector2d.
     *
     * @param value the value the Y is to be set to.
     * @author Robin van Alst.
     */
    public void setY(double value) {
        y = value;
    }

    /**
     * Returns a new vector that is the result of subtracting the given vector from this vector.
     *
     * @param other the value that is to be subtracted from the Vector2d.
     * @return the new adjusted Vector2d.
     * @author Robin van Alst.
     */
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    /**
     * Returns a new vector that is the result of adding the given vector to this vector.
     *
     * @param other the value that is to be added to the Vector2d.
     * @return the new adjusted Vector2d.
     * @author Robin van Alst.
     */
    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    /**
     * This method is used to multiply the values of the Vector2d with a given factor.
     *
     * @param factor the factor that the values of the Vector2d are to be multiplied by.
     * @return the new and adjusted Vector2d.
     * @author Robin van Alst.
     */
    public Vector2d multiply(double factor) {
        return new Vector2d(
                this.x * factor,
                this.y * factor
        );
    }

    /**
     * Returns the length of this vector using Pythagoras' equation.
     *
     * @return the diagonal of the Pythagoras triangle.
     * @author Robin van Alst.
     */
    public double vectorLength() {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * This method is used to put the values of the Vector2d in a String.
     *
     * @return the String containing the values of the Vector2d.
     * @author Robin van Alst.
     */
    @Override
    public String toString() {
        return x + ", " + y;
    }

    /**
     * This method returns the bearing and the distance in the form of a new Vector2d.
     *
     * @param bearing  the bearing used to create the new Vector2d.
     * @param distance the distance used to create the new Vector2d.
     * @return the new Vector2d.
     * @author Robin van Alst.
     */
    public static Vector2d getFromBearingAndDistance(double bearing, double distance) {
        double x;
        double y;
        x = Math.sin(Math.toRadians(bearing)) * distance;
        y = Math.cos(Math.toRadians(bearing)) * distance;
        return new Vector2d(x, y);
    }

    /**
     * This method is used to get the angle of the Vector2d.
     *
     * @return the angle of the Vector2d.
     * @author Robin van Alst.
     */
    public double getWorldBearing() {
        double result = (Math.toDegrees(Math.atan2(x, y)));
        if (result < 0) {
            result += 360;
        }
        return result;
    }

    /**
     * this method returns the angle that is to be turned to to face the Vector2d.
     *
     * @param angle the angle that is faced to.
     * @return the new Vector2d that is to be faced to.
     * @author Robin van Alst.
     */
    public Vector2d rotate(double angle) {
        double length = vectorLength();
        double bearing = getWorldBearing();
        bearing += angle;
        return Vector2d.getFromBearingAndDistance(bearing, length);
    }

    /**
     * this method is used to get the distance to a Vector2d.
     *
     * @param vector1 the Vector2d that the robot is currently at.
     * @param vector2 the Vector2 that the robot wants to know it's distance to.
     * @return the distance between the 2 Vector2d's.
     * @author Justin Hoogstraate.
     */
    public static double getDistanceTo(Vector2d vector1, Vector2d vector2) {
        double deltaX = vector1.getX() - vector2.getX();
        double deltaY = vector1.getY() - vector2.getY();
        return Math.sqrt((deltaX * deltaX) + (deltaY + deltaY));
    }
}
