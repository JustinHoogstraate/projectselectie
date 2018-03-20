package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import robocode.HitRobotEvent;
import hybridattack.Generic.HybridAttackBase;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

public class Charger extends HybridAttackBase {

    @Override
    public void run() {
        ArrayList<RobotReference> enemies;
        RobotReference lowestHealth = null;
        while (true) {
            super.run();
            enemies = getEnemies();
            if (enemies.size() > 1 && getTeam().size() > 1) {
                for (RobotReference enemy : enemies) {
                    if (lowestHealth == null || enemy.getEnergy() < lowestHealth.getEnergy()) {
                        if (enemy != teamTarget) {
                            lowestHealth = enemy;
                        }
                        if (!lowestHealth.isTeammate()) {
                            turnLeft(lowestHealth.getLocation().subtract(location).getWorldBearing() + getHeading());
                            ahead(500);

                    }

                    }

                }


            }

        }
    }


    /* TODO
    * Locking taget with low health
    * locking target which is not a target for other enemies
    *
    * */


    /*
    On hit, Fire and hit him again
    We want to kill the robot bij hitting him for additional bonus points.
    Bullet damage = 4 * firepower. If firepower > 1, it does an additional damage = 2 * (power - 1).
     */

    public void onHitRobot(HitRobotEvent e) {

        if (!isTeammate(e.getName())) {

            if (e.getEnergy() > 16) {
                fire(3);
            } else if (e.getEnergy() > 13) {
                fire(2.5);
            } else if (e.getEnergy() > 10) {
                fire(2);
            } else if (e.getEnergy() > 7) {
                fire(1.5);
            } else if (e.getEnergy() > 4) {
                fire(1);
            } else if (e.getEnergy() > 2) {
                fire(.5);
            } else if (e.getEnergy() > .6) {
                fire(.1);
            }
            ahead(50);
        }

    }
}
