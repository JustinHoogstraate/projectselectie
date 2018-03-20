package samplerobotvanrobin;

import hybridattack.Generic.Vector2d;

public class MovementController extends RobotController {
    private boolean reverse = false;

    public MovementController(SMILE owner) {
        super(owner);
    }

    @Override
    public void update() {
        circleStrafeTarget();
    }

    private void circleStrafeTarget() {
        EnemyReference target = owner.getCurrentTarget();
        RobotReference closest = owner.getClosestRobot();
        if (target != null) {
            Vector2d myWorldLocation = owner.getWorldLocation();
            Vector2d targetWorldLocation = target.getWorldLocation();
            Vector2d targetDelta = myWorldLocation.subtract(targetWorldLocation);

            Vector2d closestWorldLocation = closest.getWorldLocation();
            Vector2d closestDelta = myWorldLocation.subtract((closestWorldLocation));


            boolean doAvoidRobot = false;
            double distanceToTarget = targetDelta.vectorLength();
            double distanceToClosest = closestDelta.vectorLength();
            double bearingToTarget = targetDelta.getWorldBearing();
            double bearingToClosest = closestDelta.getWorldBearing();
            double movementHeading = bearingToTarget;
            double myHeading = owner.getHeading();
            if (reverse) {
                myHeading -= 180;
                if (myHeading < 0) {
                    myHeading += 360;
                }
            }
            double myVelocity = owner.getVelocity();

            double preferredTargetDistance = myVelocity * 20;
            doAvoidRobot = distanceToClosest < distanceToTarget && distanceToClosest < preferredTargetDistance;

            if (distanceToTarget > preferredTargetDistance + 100) {
                movementHeading += 45;
            } else if (distanceToTarget < preferredTargetDistance - 100) {
                movementHeading += 135;
            } else {
                movementHeading += 90;
            }

            double relativeBearing = movementHeading - myHeading;
            if (relativeBearing > 0) {
                owner.setTurnRight(relativeBearing);
            } else {
                owner.setTurnLeft(Math.abs(relativeBearing));
            }

            if (atRoomEdge() && headingToRoomEdge()) {
                reverse = !reverse;
            }

            if (distanceToClosest < 100) {
                System.out.println("Distance to closest < 50!");
                if (myHeading > bearingToClosest - 45 && myHeading < bearingToClosest + 45) {
                    reverse = !reverse;
                }
            }

            if (reverse) {
                owner.setBack(100);
            } else {
                owner.setAhead(100);
            }
        }
    }

    private boolean atRoomEdge() {
        if (owner.getX() < 50) {
            return true;
        }
        if (owner.getY() < 50) {
            return true;
        }
        if (owner.getX() > owner.getBattleFieldWidth() - 50) {
            return true;
        }
        if (owner.getY() > owner.getBattleFieldHeight() - 50) {
            return true;
        }
        return false;
    }

    private boolean headingToRoomEdge() {
        double absHeading = Math.abs(owner.getHeading());
        if (reverse) {
            absHeading -= 180;
        }
        if (absHeading < 0) {
            absHeading += 360;
        }
        if (owner.getX() < 100) {
            return absHeading > 180 && absHeading <= 360;
        }
        if (owner.getY() < 100) {
            return absHeading > 90 && absHeading <= 270;
        }
        if (owner.getX() > owner.getBattleFieldWidth() - 100) {
            return absHeading > 0 && absHeading <= 180;
        }
        if (owner.getY() > owner.getBattleFieldHeight() - 100) {
            return absHeading > 270 || absHeading <= 90;
        }
        return false;
    }
}
