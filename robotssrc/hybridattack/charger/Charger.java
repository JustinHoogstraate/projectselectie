package hybridattack.charger;

import hybridattack.Generic.RobotReference;
import hybridattack.Generic.Vector2d;
import robocode.*;
import hybridattack.Generic.HybridAttackBase;

import java.util.ArrayList;

/**
 * This robot is our "Charger" , it tries to ram the enemies while hooting at them.
 *
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

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


    /**
     * This method handles the event of a robot boeing scanned.
     *
     * @param event this event is created when a robot is scanned.
     * @author Vincent Luiten, Thomas Heinsbroek.
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);
    }

    /**
     * This method checks if there already is a target set by the TargetLeaders, and sets a different target if there is.
     * The Target set by this method is the closest non-friendly, non-TargetLeaderTarget.
     *
     * @author Justin Hoogstraate, Thomas Heinsbroek & Vincent Luiten.
     */
    public void setChargerTarget() {
        if (getEnemies().size() > 0) {
            ArrayList<RobotReference> enemies = getEnemies();
            RobotReference closestEnemy = enemies.get(0);
            if (chargerTarget == null && teamTarget != null) {
                for (RobotReference enemy : enemies) {
                    if (!enemy.isTeammate()) {
                        if (Vector2d.getDistanceTo(closestEnemy.getLocation(), getLocation()) > Vector2d.getDistanceTo(enemy.getLocation(), getLocation()))
                            closestEnemy = enemy;
                    }
                }
                chargerTarget = closestEnemy;
            } else {
                chargerTarget = enemies.get(0);
            }
        }
    }

    /**
     * This method lets the Charger attack its target.
     *
     * @param target This is the enemy to be focused upon by the Charger(s).
     * @author Vincent Luiten, Thomas Heinsbroek.
     */
    public void attack(RobotReference target) {
        if (target != null) {
            turnToVector(target.getLocation());
            pointGunToVector(target.getLocation());
            setAhead(60);
            fire(3);


        }
    }

    /**
     * This method detects when the Charger misses a bullet, so it can re-aim its gun at its target.
     *
     * @param event the event generated when a bullet is missed.
     * @author Vincent Luiten, Thomas Heinsbroek.
     */
    public void onBulletMissed(BulletMissedEvent event) {
        pointGunToVector(chargerTarget.getLocation());
        missed = true;
    }

    /**
     * This method detects if a bullet hits a robot, and if it hit a friendly robot it relocates itself to alter the trajectory of its bullets.
     *
     * @param event the event generated when a bullet hits a robot.
     * @author Vincent Luiten, Thomas Heinsbroek.
     */
    public void onBulletHit(BulletHitEvent event) {
        RobotReference hitRobot;
        hitRobot = robots.get(event.getName());

        if (hitRobot.isTeammate()) {
            setTurnRight(90);
            setAhead(400);
            setChargerTarget();
        }
    }

    /**
     * This method detects the collision between a charger and another robot.
     * If it's a friendly it relocates itself, and resets its target.
     * If it's an enemy it continues ramming it.
     *
     * @param event This event is generated when the Charger collides with another robot.
     * @author Vincent Luiten, Thomas Heinsbroek.
     */
    public void onHitRobot(HitRobotEvent event) {


        if (!isTeammate(event.getName())) {
            target = robots.get(event.getName());
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
