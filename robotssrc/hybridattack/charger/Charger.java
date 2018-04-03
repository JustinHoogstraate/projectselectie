package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import hybridattack.Generic.Vector2d;
import robocode.BulletHitEvent;
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
            if (chargerTarget != null) {
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
            RobotReference closestEnemy = enemies.get(0);
            if (chargerTarget == null && teamTarget != null) {
                for (RobotReference enemy : enemies) {
                    if (enemy != teamTarget) {
                        if (Vector2d.getDistanceTo(closestEnemy.getLocation(), location) > Vector2d.getDistanceTo(enemy.getLocation(), location))
                            closestEnemy = enemy;

                    }
                }
                chargerTarget = closestEnemy;
            } else {
                chargerTarget = enemies.get(0);
            }
        }
    }


    public void attack(RobotReference target) {
        if (target != null) {
            turnToVector(target.getLocation());
            pointGunToVector(target.getLocation());
            setAhead(60);
            fire(3);
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

    public void onBulletHit(BulletHitEvent e) {
        RobotReference hitRobot;
        hitRobot = robots.get(e.getName());

        if (hitRobot.isTeammate()) {
            setTurnRight(90);
            setAhead(400);
            setChargerTarget();
        }
    }

    public void onHitRobot(HitRobotEvent e) {


        if (!isTeammate(e.getName())) {
            target = robots.get(e.getName());
            if (getGunTurnRemaining() == 0) {
                fire(3);
                ahead(60);
            }


        } else {
            turnRight(90);
            setAhead(1000);
            setChargerTarget();
        }
        if (missed) {
            pointGunToVector(target.getLocation());
            fire(3);
        }
    }
}
