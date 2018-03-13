package samplerobotvanrobin;

import robocode.ScannedRobotEvent;

public class ScannerController extends RobotController {
    private RobotManager robotManager;

    public ScannerController(SMILE owner) {
        super(owner);
        this.robotManager = new RobotManager();
    }

    @Override
    public void update() {
        doScan();
    }

    private void doScan() {
        owner.setTurnRadarRight(720);
        //owner.execute();
    }

    public void onScannedRobot(String theirName, double theirBearing, double theirDistance, double theirHeading, double theirVelocity) {
        double worldBearing = theirBearing + owner.getHeading();
        Vector2d theirLocation = Vector2d.getFromBearingAndDistance(worldBearing, theirDistance);
        Vector2d myLocation = new Vector2d(owner.getX(), owner.getY());
        theirLocation = myLocation.add(theirLocation);

        if (owner.isTeammate(theirName)) {
            RobotReference teammate = robotManager.getTeammate(theirName);
            if (teammate == null) {
                robotManager.addTeammate(theirName, theirLocation, theirVelocity, theirHeading);
            } else {
                robotManager.updateRobot(theirName, theirLocation, theirHeading, theirVelocity);
            }
        } else {
            EnemyReference enemy = robotManager.getEnemy(theirName);
            if (robotManager.getEnemy(theirName) == null) {
                robotManager.addEnemy(theirName, theirLocation, theirVelocity, theirHeading);
            } else {
                robotManager.updateRobot(theirName, theirLocation, theirVelocity, theirHeading);
            }
            if (owner.getCurrentTarget() == null) {
                owner.focusOnEnemy(enemy);
            }
        }
    }

    public void tryAddRobot(RobotReference robot) {
        if (!robotManager.hasRobot(robot.getName())) {
            robotManager.addRobot(robot);
        }
    }
    /*
    public void setTarget(EnemyReference target) {
        EnemyReference currentTarget = owner.getCurrentTarget();
        if (currentTarget == null || !currentTarget.getName().equals(target.getName())) {
            if (robotManager.hasRobot(target.getName())) {
                owner.setTarget(robotManager.getEnemy(target.getName()));
            }
            else {
                robotManager.addRobot(target);
                owner.setTarget(target);
            }
        }
    }
    */

    public RobotReference getClosestRobot() {
        RobotReference result = null;

        Vector2d location = owner.getWorldLocation();
        return robotManager.getClosestRobot(location);
    }
}
