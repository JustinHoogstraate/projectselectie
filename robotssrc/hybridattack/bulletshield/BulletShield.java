package hybridattack.bulletshield;

import hybridattack.Generic.HybridAttackBase;
import hybridattack.Generic.RobotReference;
import hybridattack.Generic.Vector2d;
import robocode.ScannedRobotEvent;

import java.util.HashMap;

public class BulletShield extends HybridAttackBase {

    private HashMap<String, Double> previousEnergyMap = new HashMap<>();

    @Override
    public void run() {
        while (true) {
            super.run();
        }
    }

    private boolean enemyHasFired(RobotReference robotReference) {
        try {
            if (robotReference.getEnergy() < previousEnergyMap.get(robotReference.getName())) {
                return true;
            }
        } catch (NullPointerException exception) {
            //Do nothing
            //There is no previous energy data
        }
        return false;
    }

    private Vector2d calculateBulletPosition(RobotReference robot) {
        double firePower = previousEnergyMap.get(robot.getName()) - robot.getEnergy();
        double bulletVelocity = 20 - 3 * firePower;
        Vector2d velocityVector = Vector2d.getFromBearingAndDistance(robot.getBearing(), bulletVelocity);
        double bulletX = robot.getLocation().getX() - (2 * velocityVector.getX());
        double bulletY = robot.getLocation().getY() - (2 * velocityVector.getY());
        Vector2d bulletPosition = new Vector2d(bulletX, bulletY);
        System.out.println("X: " + bulletX + "\nY:" + bulletY);
        return bulletPosition;
    }

    private void shootAtBullet(Vector2d bulletPosition){
        Vector2d relativeLocation = location.subtract(bulletPosition);
        double angle = relativeLocation.getWorldBearing();
        double localHeading = angle - getGunHeading();
        if (localHeading > 180) {
            localHeading -= 360;
        }
        if (localHeading > 0) {
            turnGunRight(localHeading);
        } else if (localHeading < 0) {
            turnGunLeft(localHeading * -1);
        }
        fire(0.1);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);
        RobotReference robot = robots.get(event.getName());
        if (!robot.isTeammate()) {
            if (enemyHasFired(robot)) {
                System.out.println(robot.getName() + " fired!");
                Vector2d bulletPosition = calculateBulletPosition(robot);
                shootAtBullet(bulletPosition);
            }
            previousEnergyMap.put(robot.getName(), robot.getEnergy());
        }
    }
}