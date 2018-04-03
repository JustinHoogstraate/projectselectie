package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import robocode.BulletMissedEvent;
import robocode.HitRobotEvent;
import hybridattack.Generic.HybridAttackBase;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

public class Charger extends HybridAttackBase {
    private RobotReference chargerTarget = null;
    private RobotReference target = null;
    private boolean missed = false;

    @Override
    public void run() {

        while (true) {

            setChargerTarget();
            attack(chargerTarget);
            if(chargerTarget != null){
                pointGunToVector(chargerTarget.getLocation());
            }
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
            pointGunToVector(target.getLocation());
            setAhead(-50);
           // fire(3);





        }
    }



    /*
    On hit, Fire and hit him again
    We want to kill the robot by hitting him for additional bonus points.
    Bullet damage = 4 * firepower. If firepower > 1, it does an additional damage = 2 * (power - 1).
     */


    public void onBulletMissed(BulletMissedEvent event) {
        pointGunToVector(chargerTarget.getLocation());
        missed = true;
    }

    public void onHitRobot(HitRobotEvent e) {

        target = robots.get(e.getName());


        if (!isTeammate(e.getName())) {
            if(getGunTurnRemaining() == 0) {

                fire(3);
                ahead(60);
            }
        }else { //

        }
        if (missed){
            pointGunToVector(target.getLocation());
        }
    }
}
