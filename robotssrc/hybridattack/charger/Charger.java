package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import robocode.HitRobotEvent;
import hybridattack.Generic.HybridAttackBase;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

public class Charger extends HybridAttackBase {
    private RobotReference chargerTarget = null;

    @Override
    public void run() {

        while (true) {

            setChargerTarget();
            attack(chargerTarget);
            super.run();


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
        if (getEnemies().size() > 0) {
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

//            double headingToEnemy = target.getBearingTo(getLocation());
//            turnRight(headingToEnemy - getHeading());
            turnToVector(target.getLocation());
            setAhead(100);

        }
    }



    /*
    On hit, Fire and hit him again
    We want to kill the robot bij hitting him for additional bonus points.
    Bullet damage = 4 * firepower. If firepower > 1, it does an additional damage = 2 * (power - 1).
     */


    public void onHitRobot(HitRobotEvent e) {
        setTurnGunRight(getGunHeading() - e.getBearing());
        if (!isTeammate(e.getName())) {
            fire(3);
            ahead(60);
        }


    }
}
