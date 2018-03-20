package hybridattack.bulletshield;

import hybridattack.Generic.HybridAttackBase;
import hybridattack.Generic.RobotReference;
import hybridattack.Generic.Vector2d;
import robocode.ScannedRobotEvent;

import java.util.HashMap;

public class BulletShield extends HybridAttackBase {

    private HashMap<String,Double> previousEnergyMap = new HashMap<>();

    @Override
    public void run() {
        while(true) {
            super.run();
        }
    }

    private boolean enemyHasFired(RobotReference robotReference){
        try {
            if (robotReference.getEnergy() < previousEnergyMap.get(robotReference.getName())) {
                return true;
            }
        } catch (NullPointerException exception){
            //Do nothing
            //There is no previous energy data
        }
        return false;
    }

    private Vector2d calculateShootingPosition(){
        return new Vector2d(0,0);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);
        RobotReference robot = robots.get(event.getName());
        if(robot.isTeammate()) {
            if (enemyHasFired(robot)) {
                System.out.println(robot.getName() + " fired!");
                Vector2d shootingPosition = calculateShootingPosition();
            }
            previousEnergyMap.put(robot.getName(), robot.getEnergy());
        }
    }
}