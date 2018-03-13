package samplerobotvanrobin;

import robocode.*;

import java.io.IOException;
import java.util.ArrayList;

public class SMILE extends TeamRobot {
    private ScannerController scanner;
    private MovementController movement;
    private GunController gun;
    private EnemyReference target;

    public EnemyReference getCurrentTarget() {
        return target;
    }

    public void setTarget(EnemyReference target) {
        this.target = target;
    }

    public RobotReference getClosestRobot() {
        return scanner.getClosestRobot();
    }

    public Vector2d getWorldLocation() {
        return new Vector2d(getX(), getY());
    }

    /*
    private final int INIT_SCAN_ANGLE = 360;
    private final int SCAN_TIMEOUT = 100;
    private final int PREFERRED_TARGET_DISTANCE = 250;
    private final int MINIMUM_ENEMY_DISTANCE = 100;
    private final int MINIMUM_SAFE_ENEMY_DISTANCE = 200;
    private final int MAXIMUM_CHARGE_DISTANCE = 500;
    private final int ROOM_WIDTH = 800;
    private final int ROOM_HEIGHT = 600;
    private int turnsAgoSinceLastScan = 0;
    private final int BULLET_POWER = 2;
    private double targetLeadingMultiplier = -8;
    */

    /*
    private int movementSpeed = 1;
    private String chargeState = "idle";
    */

    /*
    private double currentBearing = 0.0;
    */

    /*
    private ArrayList<RobotReference> robots = new ArrayList();
    private ArrayList<RobotReference> enemies = new ArrayList();
    private ArrayList<RobotReference> teammates = new ArrayList();

    boolean aimedAtTarget = false;
    */

    public SMILE() {
        scanner = new ScannerController(this);
        gun = new GunController(this);
        movement = new MovementController(this);
    }

    public void run() {
        while (true) {
            //doScan();

            scanner.update();
            gun.update();
            movement.update();

            execute();
        }
    }



    public void onScannedRobot(ScannedRobotEvent e) {
        scanner.onScannedRobot(e.getName(), e.getBearing(), e.getDistance(), e.getHeading(), e.getVelocity());
    }

    public void focusOnEnemy(EnemyReference target) {
        this.target = target;

        try {
            FocusOnEnemyMessage message = new FocusOnEnemyMessage(target);
            broadcastMessage(message);
        } catch (IOException e) {
            System.out.println("Failed to broadcast focusOnEnemyMessage: " + e);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof FocusOnEnemyMessage) {
            FocusOnEnemyMessage message = (FocusOnEnemyMessage) e.getMessage();
            EnemyReference target = message.getTarget();

            this.target = target;
            scanner.tryAddRobot(target);
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if (target.getName().equals(e.getName())) {
            target = null;
        }
    }
    /*
        private void aimAtTargetedEnemy() {
            if (focusOnThisOne != null) {
                double angle = gun.getAimAngle(focusOnThisOne, getMyLocation());
                double bodyHeading = getHeading();
                double gunHeading = getGunHeading();
                double localHeading = angle - gunHeading;
                if (localHeading > 0) {
                    setTurnGunRight(localHeading);
                } else if (localHeading < 0) {
                    setTurnGunLeft(localHeading * -1);
                }
                if (Math.abs(localHeading) <= 3) {
                    aimedAtTarget = true;
                }
            }

            aimedAtTarget = false;
            if (focusOnThisOne != null) {
                Vector2d targetLocation = focusOnThisOne.getLocation();
                Vector2d myLocation = getMyLocation();
                Vector2d relativeLocation = myLocation.subtract(targetLocation);
                Vector2d targetLeadingVector = getTargetLeadingVector();
                relativeLocation = relativeLocation.add(targetLeadingVector);
                System.out.println(targetLeadingVector);
                double targetBearing = relativeLocation.getWorldBearing();
                double bodyHeading = getHeading();
                double gunHeading = getGunHeading();
                double localHeading = targetBearing - gunHeading;
                if (localHeading > 0) {
                    setTurnGunRight(localHeading);
                } else if (localHeading < 0) {
                    setTurnGunLeft(localHeading * -1);
                }
                if (Math.abs(localHeading) <= 3) {
                    aimedAtTarget = true;
                }
                /*System.out.println("World bearing to target '" + focusOnThisOne.getName() + "': " + worldBearing);
                double localBearing = worldBearing - getHeading() - getGunHeading();
                System.out.println("Local bearing to target '" + focusOnThisOne.getName() + "': " + localBearing);
                setTurnGunRight(localBearing);*/
            /*if (localBearing > 0) {

            }
            else if (localBearing < 0){
                setTurnGunLeft(localBearing);
            }*//*
        }
    }
*/



    /*
        private void chargeTarget() {
            try {
                if (focusOnThisOne != null) {
                    Vector2d theirLocation = focusOnThisOne.getWorldLocation();
                    Vector2d myLocation = getWorldLocation();
                    Vector2d delta = myLocation.subtract(theirLocation);
                    double bearing = delta.getWorldBearing();
                    double heading = getHeading();
                    double distanceToTarget = delta.vectorLength();
                    if (movementSpeed < 0) {
                        bearing += 180;
                        heading += 180;
                    }
                    switch(chargeState) {
                        case "idle":
                            if (focusOnThisOne != null) {
                                chargeState = "charging";
                            }
                            break;
                        case "charging":
                            if (distanceToTarget < MINIMUM_ENEMY_DISTANCE) {
                                chargeState = "retreating";
                            }
                            else {
                                bearing += 20 * movementSpeed;
                            }
                            break;
                        case "retreating":
                            if (distanceToTarget > MAXIMUM_CHARGE_DISTANCE || atRoomEdge()) {
                                chargeState = "idle";
                                movementSpeed *= -1;
                            }
                            else {
                                bearing += 135 * movementSpeed;
                            }
                            break;
                        default:
                            break;
                    }

                    double relativeBearing = bearing - heading;
                    if (relativeBearing > 0) {
                        setTurnRight(relativeBearing);
                    }
                    else {
                        setTurnLeft(Math.abs(relativeBearing));
                    }
                    if (movementSpeed > 0) {
                        setAhead(100);
                    }
                    else {
                        setBack(100);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        private void patrolEdges() {
            try {
                if (!atRoomEdge()) {
                    currentBearing = getBearingToClosestEdge();
                } else {
                    if (getX() > getBattleFieldWidth() - 50 && currentBearing == 90) {
                        currentBearing = 0;
                        System.out.println("turning up");
                    } else if (getY() > getBattleFieldHeight() - 50 && currentBearing == 0) {
                        currentBearing = 270;
                        System.out.println("turning left");
                    } else if (getX() < 50 && currentBearing == 270) {
                        currentBearing = 180;
                        System.out.println("turning down");
                    } else if (getY() < 50 && currentBearing == 180) {
                        currentBearing = 90;
                        System.out.println("turning right");
                    }
                }
                double deltaBearing = currentBearing - getHeading();
                if (deltaBearing > 0) {
                    setTurnRight(deltaBearing);
                } else {
                    setTurnLeft(Math.abs(deltaBearing));
                }
                if (getTurnRemaining() == 0) {
                    setAhead(10);
                } else {
                    setAhead(0);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    */

/*
    private Vector2d getClosestRobotDistance() {
        Vector2d closestDistance = null;
        for (RobotReference robot : robots) {
            Vector2d distance = getDistanceFromRobot(robot);
            if (closestDistance == null || distance.vectorLength() < closestDistance.vectorLength()) {
                closestDistance = distance;
            }
        }
        return closestDistance;
    }

    private double getBestAvoidBearing() {
        double average = 0.0;
        for (RobotReference robot : robots) {
            Vector2d distance = getDistanceFromRobot(robot);
            if (distance.vectorLength() < MINIMUM_SAFE_ENEMY_DISTANCE) {
                average += distance.getWorldBearing();
            }
        }
        return (average / robots.size()) + 90;
    }

    private Vector2d getDistanceFromRobot(RobotReference other) {
        Vector2d theirLocation = other.getWorldLocation();
        Vector2d myLocation = getWorldLocation();
        Vector2d delta = myLocation.subtract(theirLocation);
        return delta;
    }

    private double getBearingToClosestEdge() {
        Vector2d top = new Vector2d(getX(), ROOM_HEIGHT);
        Vector2d left = new Vector2d(0, getY());
        Vector2d right = new Vector2d(ROOM_WIDTH, getY());
        Vector2d bottom = new Vector2d(getX(), 0);

        Vector2d myLocation = getWorldLocation();

        Vector2d[] deltas = new Vector2d[]{
                myLocation.subtract(top),
                myLocation.subtract(right),
                myLocation.subtract(left),
                myLocation.subtract(bottom)
        };

        Vector2d closest = null;
        for (Vector2d delta : deltas) {
            if (closest == null || delta.vectorLength() < closest.vectorLength()) {
                closest = delta;
            }
        }
        return closest.getWorldBearing();
    }

    private boolean atRoomEdge() {
        if (getX() < 50) {
            return true;
        }
        if (getY() < 50) {
            return true;
        }
        if (getX() > getBattleFieldWidth() - 50) {
            return true;
        }
        if (getY() > getBattleFieldHeight() - 50) {
            return true;
        }
        return false;
    }

    double distanceBetween(Vector2d a, Vector2d b) {
        return a.subtract(b).vectorLength();
    }

    private Vector2d getMyVelocity() {
        double heading = getHeading();
        double velocity = getVelocity();
        return Vector2d.getFromBearingAndDistance(heading, velocity);
    }
    */
}
