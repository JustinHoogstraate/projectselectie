package hybridattack.targetleader;

import hybridattack.Generic.*;
import robocode.MessageEvent;

import java.io.IOException;

/**
 * This Robot tries to stick to a chosen location on the map, while setting a target to focus and firing upon that target.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class TargetLeader extends HybridAttackBase {
    private boolean shouldDoDodge = false;
    private Vector2d dodgeAroundLocation = null;

    private final int PREFERRED_ENEMY_DISTANCE = 200;
    private final int TARGET_LOCATION_MARGIN = 30;

    protected Vector2d desiredVelocity = new Vector2d(0, 0);
    protected Vector2d targetLocation = null;

    private boolean isAtTargetLocation() {
        if (targetLocation != null &&
                getLocation().subtract(targetLocation).vectorLength() <= TARGET_LOCATION_MARGIN) {
            return true;
        }
        return false;
    }


    public TargetLeader() {
        super();
    }

    private void setTargetLocation(int x, int y) {
        if (targetLocation == null || targetLocation.getX() != x || targetLocation.getY() != y) {
            System.out.println("Setting target location to " + x + ", " + y);
            this.targetLocation = new Vector2d(x, y);
            try {
                broadcastMessage(new SetTargetLocationMessage(x, y));
            } catch (IOException ex) {
                ; //ignore
            }
        }
    }

    @Override
    public void run() {
        if (targetLocation == null) {
            int x = (int) (getBattleFieldWidth() * 0.25);
            int y = (int) (getBattleFieldHeight() * 0.5);
            setTargetLocation(x, y);
        }
        while (true) {
            doDodge();
            //dodgeWalls();
            //doMove();
            fireAtTarget();
            super.run();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);
        if (!event.getSender().equals(getName())) {
            if (event.getMessage() instanceof EnemyFiredMessage) {
                EnemyFiredMessage message = (EnemyFiredMessage) event.getMessage();
                dodgeAroundLocation = message.getFiredFromLocation();
                shouldDoDodge = true;
            } else if (event.getMessage() instanceof SetTargetLocationMessage) {
                if (event.getSender().compareTo(getName()) > 0) {
                    SetTargetLocationMessage message = (SetTargetLocationMessage) event.getMessage();
                    int targetX = (int) (getBattleFieldWidth() - message.getX());
                    setTargetLocation(targetX, (int) (getBattleFieldHeight() / 2));
                }
            }
        }
    }

    private void doDodge() {
        if (shouldDoDodge && dodgeAroundLocation != null && getDistanceRemaining() == 0) {
            Vector2d delta = null;
            double dodgeAngle = 0.0;

            if (isAtTargetLocation()) {
                delta = dodgeAroundLocation.subtract(getLocation());
                setBack(delta.vectorLength());
            } else {
                delta = targetLocation.subtract(getLocation());
                setAhead(100);
            }

            dodgeAngle = delta.getWorldBearing();
            dodgeAngle -= getHeading();

            while(dodgeAngle > 180) {
                dodgeAngle -= 360;
            }

            if (dodgeAngle > 0) {
                setTurnRight(dodgeAngle);
            }
            else if (dodgeAngle < 0) {
                setTurnLeft(Math.abs(dodgeAngle));
            }

            /*
            if (delta.vectorLength() > PREFERRED_ENEMY_DISTANCE) {
                dodgeAngle -= 20;
            }
            else if (delta.vectorLength() < PREFERRED_ENEMY_DISTANCE) {
                dodgeAngle += 20;
            }
            */

            /*
            while (dodgeAngle > 360) {
                dodgeAngle -= 360;
            }
            desiredVelocity = Vector2d.getFromBearingAndDistance(dodgeAngle, 100);
            */
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
            double firepower = Math.min(400 / Vector2d.getDistanceTo(teamTarget.getLocation(), location), 3);
            targetLeading();
            fire(firepower);
        }
    }

    private void targetLeading() {
        Vector2d velocity = teamTarget.getVelocity();
        Vector2d currentLocation = teamTarget.getLocation();
        double predictionX = currentLocation.getX() + 5 * velocity.getX();
        double predictionY = currentLocation.getY() + 5 * velocity.getY();
        Vector2d prediction = new Vector2d(predictionX, predictionY);
        pointGunToVector(prediction);
    }

    /*
    private void preventFromRammingObstacle(int obstacle) {
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
//        System.out.println(obstacle + "");
        preventFromRammingObstacle(obstacle);
    }
*/
    /**
     * Moves the robot at the current desiredVelocity.
     *//*
    protected void doMove() {
        if (desiredVelocity.vectorLength() > 0) {
            double bearing = desiredVelocity.getWorldBearing();
            double heading = getHeading();
            double relativeDodgeAngle = bearing - getHeading();

            if (relativeDodgeAngle > 0) {
                setTurnRight(relativeDodgeAngle);
            } else {
                setTurnLeft(Math.abs(relativeDodgeAngle));
            }

            if (shouldDoDodge) {
                if (forward) {
                    setAhead(100);
                } else {
                    setBack(100);
                }
                shouldDoDodge = false;
            }
        }
    }
*/
    /**
     * Attempts to move away from any nearby walls.
     *//*
    private void dodgeWalls() {
        for (int wall : getNearbyObstacles()) {
            onNearObstacle(wall);
        }
    }*/
}
