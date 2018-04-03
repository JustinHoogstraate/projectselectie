package hybridattack.targetleader;

import hybridattack.Generic.EnemyFiredMessage;
import hybridattack.Generic.HybridAttackBase;
import hybridattack.Generic.SetTargetMessage;
import hybridattack.Generic.Vector2d;
import robocode.MessageEvent;
import samplerobotvanrobin.RobotReference;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TargetLeader extends HybridAttackBase {
    private boolean shouldDoDodge = false;
    private Vector2d dodgeAroundLocation = null;

    private final int PREFERRED_ENEMY_DISTANCE = 200;

    protected Vector2d desiredVelocity = new Vector2d(0, 0);

    @Override
    public void run() {
        while (true) {
            doDodge();
            dodgeWalls();
            doMove();
            fireAtTarget();
            super.run();
            fireAtTarget();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);
        if (event.getMessage() instanceof EnemyFiredMessage) {
            EnemyFiredMessage message = (EnemyFiredMessage) event.getMessage();
            dodgeAroundLocation = message.getFiredFromLocation();
            shouldDoDodge = true;
        }
    }

    private void doDodge() {
        if (shouldDoDodge && dodgeAroundLocation != null) {
            Vector2d delta = dodgeAroundLocation.subtract(getLocation());
            double dodgeAngle = delta.getWorldBearing() + 90;
            if (delta.vectorLength() > PREFERRED_ENEMY_DISTANCE) {
                dodgeAngle -= 20;
            }
            else if (delta.vectorLength() < PREFERRED_ENEMY_DISTANCE) {
                dodgeAngle += 20;
            }

            while (dodgeAngle > 360) {
                dodgeAngle -= 360;
            }
            desiredVelocity = Vector2d.getFromBearingAndDistance(dodgeAngle, 100);
        }
    }

    @Override
    protected void setTeamTarget(hybridattack.Generic.RobotReference reference) {
        if (teamTarget == null && !reference.isTeammate() && teamTarget != reference) {
            teamTarget = reference;
            try {
                broadcastMessage(new SetTargetMessage(teamTarget));
            } catch (IOException ioe) {
            }
        }

    }

    @Override
    protected void onEnemyFired(Vector2d location) {
        super.onEnemyFired(location);
        shouldDoDodge = true;
        dodgeAroundLocation = location;
    }

    private void fireAtTarget() {
        if (teamTarget != null) {
            targetLeading();
            double firepower = Math.min(400 / Vector2d.getDistanceTo(teamTarget.getLocation(), location), 3);
            fire(firepower);
        }
    }

    private void targetLeading() {
        System.out.println(teamTarget.getName());
        Vector2d velocity = teamTarget.getVelocity();
        Vector2d currentLocation = teamTarget.getLocation();
        double predictionX = currentLocation.getX() + 10 * velocity.getX() * Math.sin(Math.toRadians(teamTarget.getHeading()));
        double predictionY = currentLocation.getY() + 10 * velocity.getY() * Math.cos(Math.toRadians(teamTarget.getHeading()));
        Vector2d prediction = new Vector2d(predictionX, predictionY);
        pointGunToVector(prediction);
    }

    private void preventFromRammingObstacle(int obstacle/*, boolean forward*/) {
        Vector2d velocity = getVelocity2d();
        //int mult = (forward) ? 1 : -1;
        switch (obstacle) {
            case TOP_WALL:
                if (velocity.getY() > 0) {
                    if (desiredVelocity.getY() > 0) {
                        desiredVelocity.setY(desiredVelocity.getY() * -1);
                    }
                    //reverse();
                }
                break;
            case RIGHT_WALL:
                if (velocity.getX() > 0) {
                    if (desiredVelocity.getX() > 0) {
                        desiredVelocity.setX(desiredVelocity.getX() * -1);
                    }
                    //reverse();
                }
                break;
            case BOTTOM_WALL:
                if (velocity.getY() < 0) {
                    if (desiredVelocity.getY() < 0) {
                        desiredVelocity.setY(desiredVelocity.getY() * -1);
                    }
                }
                break;
            case LEFT_WALL:
                if (velocity.getX() < 0) {
                    if (desiredVelocity.getX() < 0) {
                        desiredVelocity.setX(desiredVelocity.getX() * -1);
                    }
                }
                break;
            case NEAR_ROBOT:
                if (desiredVelocity.vectorLength() > 0) {
                    desiredVelocity = desiredVelocity.rotate(45);
                }
                break;
            default:
                break;
        }
    }

    public void onNearObstacle(int obstacle) {
        System.out.println(obstacle + "");
        preventFromRammingObstacle(obstacle);
    }

    /**
     * Moves the robot at the current desiredVelocity.
     */
    protected void doMove() {
        if (desiredVelocity.vectorLength() > 0) {
            double bearing = desiredVelocity.getWorldBearing();
            double heading = getHeading();
            double relativeDodgeAngle = bearing - getHeading();
/*
            if (relativeDodgeAngle < 90 && relativeDodgeAngle > -90) {
                //forward = true;
            } else {
                //forward = false;
                relativeDodgeAngle += 180;
                if (relativeDodgeAngle >= 180) {
                    relativeDodgeAngle -= 360;
                }
            }*/

            if (relativeDodgeAngle > 0) {
                setTurnRight(relativeDodgeAngle);
            } else {
                setTurnLeft(Math.abs(relativeDodgeAngle));
            }

            if (shouldDoDodge) {
                setAhead(100);
                /*if (forward) {
                    setAhead(100);
                } else {
                    setBack(100);
                }*/
                shouldDoDodge = false;
            }
        }
    }

    /**
     * Attempts to move away from any nearby walls.
     */
    private void dodgeWalls() {
        for (int wall : getNearbyObstacles()) {
            onNearObstacle(wall);
        }
    }
}
