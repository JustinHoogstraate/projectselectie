package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import hybridattack.Generic.Vector2d;
import robocode.HitRobotEvent;
import hybridattack.Generic.HybridAttackBase;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Charger extends HybridAttackBase {
    private RobotReference chargerTarget = null;

    @Override
    public void run() {

        while (true) {
            super.run();
            setChargerTarget();
            attack(chargerTarget);


        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);

    }

    /* TODO
    * Locking taget with low health
    * locking target which is not a target for other enemies
    *
    * */

    public void setChargerTarget() {
        if(getEnemies().size() > 0 ) {
            ArrayList<RobotReference> enemies = getEnemies();
            if (chargerTarget == null && teamTarget != null) {
                for (RobotReference enemy : enemies) {
                    if (enemy != teamTarget) {
                        chargerTarget = enemy;
                    }
                }
            } else {
                chargerTarget = enemies.get(0);
            }
        }
    }

    public void attack(RobotReference target) {
        if (target != null) {
            double headingToEnemy = target.getHeading();
            TurnToVec
            turnRight(headingToEnemy);
            ahead(500);
        }
    }



    /*
    On hit, Fire and hit him again
    We want to kill the robot bij hitting him for additional bonus points.
    Bullet damage = 4 * firepower. If firepower > 1, it does an additional damage = 2 * (power - 1).
     */


//    public void onHitRobot(HitRobotEvent e) {
//        turnRight(e.getBearing());
//        turnGunRight(e.getBearing());
//
//        if (e.getEnergy() > 16) {
//            fire(3);
//        } else if (e.getEnergy() > 13) {
//            fire(2.5);
//        } else if (e.getEnergy() > 10) {
//            fire(2);
//        } else if (e.getEnergy() > 7) {
//            fire(1.5);
//        } else if (e.getEnergy() > 4) {
//            fire(1);
//        } else if (e.getEnergy() > 2) {
//            fire(.5);
//        } else if (e.getEnergy() > .6) {
//            fire(.1);
//        }
//        ahead(60);
//        turnGunRight(e.getBearing());


    //}
}
