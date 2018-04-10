package hybridattack.targetleader;

import hybridattack.Generic.*;
import robocode.MessageEvent;
import hybridattack.Generic.Vector2d;
import hybridattack.Generic.RobotReference;

import java.io.IOException;

/**
 * <p>
 * The TargetLeader is a specialized gunner/attacker robot with two core features;
 * <ol>
 * <li>The robot's namesake Target Leading behavior;</li>
 * <li>Smart dodging of enemy projectiles, while staying near a preset target location.</li>
 * </ol>
 * This system allows the TargetLeader to stay out of enemy fire while firing volleys of
 * accurate fire at enemies.
 * </p>
 * <p>
 * When a pair of TargetLeaders are placed in the same team, they will work together
 * to guard opposite sides of the battlefield, while picking off targets one at a time.
 * They share a common target at all times, and will focus their combined firepower on this target
 * until it is destroyed.
 * </p>
 *
 * @author Robin 'Batman' van Alst, Justin Hoogstraate
 */
public class TargetLeader extends HybridAttackBase {
    //private variables;
    //constant maximum distance to targetLocation
    private final int TARGET_LOCATION_MARGIN = 30;
    //Should we do a dodge?
    private boolean shouldDoDodge = false;
    //The location that we wish to dodge around.
    private Vector2d dodgeAroundLocation = null;
    //The target location that we want to stay near.
    private Vector2d targetLocation = null;

    /**
     * The TargetLeader's constructor has no extra paramerers, nor does it have any defining behavior.
     *
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    public TargetLeader() {
        super();
    }

    /**
     * This method checks whether the robot is near its target location or not.
     * It uses a constant integer value named TARGET_LOCATION_MARGIN as the maximum distance
     * to this target location; any distance greater than this margin is considered 'away from
     * target location'.
     *
     * @return
     */
    private boolean isAtTargetLocation() {
        if (targetLocation != null &&
                getLocation().subtract(targetLocation).vectorLength() <= TARGET_LOCATION_MARGIN) {
            return true;
        }
        return false;
    }

    /**
     * This method assigns the given X and Y location to this robot's target location.
     * This is the location that the robot attempts to stay near at all times, to prevent
     * it from driving around wildly and thus getting stuck on robots and walls.
     *
     * @param x
     * @param y
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    private void setTargetLocation(int x, int y) {
        if (targetLocation == null || targetLocation.getX() != x || targetLocation.getY() != y) {
            System.out.println("Setting target location to " + x + ", " + y);
            this.targetLocation = new Vector2d(x, y);
            try {
                broadcastMessage(new SetTargetLocationMessage(x, y));
            } catch (IOException ex) {
                ; //ignore
            }
        }
    }

    /**
     * This method override is the main run method of the TargetLeader. <br/>
     * It attempts to do a few things in order;<br/>
     * <ol>
     * <li>Assign a target location. This is the location that the robot will stay near.</li>
     * <li>Main run loop attempts to dodge when necessary, fire at targets, <br/>
     * and finally calls the base class' run method.</li>
     * </ol>
     *
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    @Override
    public void run() {
        if (targetLocation == null) {
            int x = (int) (getBattleFieldWidth() * 0.25);
            int y = (int) (getBattleFieldHeight() * 0.5);
            setTargetLocation(x, y);
        }
        while (true) {
            doDodge();
            fireAtTarget();
            super.run();
        }
    }

    /**
     * This method override provides TargetLeader-specific message behavior so that it can
     * respond to some message types specific to the TargetLeader.
     *
     * @param event
     * @author Robin 'Batman' van Alst
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage() instanceof EnemyFiredMessage) {
            //Message is an EnemyFiredMessage, so we should do a dodge. The robot sets
            // the shouldDoDodge flag, so that the doDodge() method knows to dodge.
            EnemyFiredMessage message = (EnemyFiredMessage) event.getMessage();
            dodgeAroundLocation = message.getFiredFromLocation();
            shouldDoDodge = true;
        } else if (event.getMessage() instanceof SetTargetLocationMessage) {
            //The message is a SetTargetLocationMessage, so we should calculate the opposite
            //of the received target location, then set that as the new target location.
            if (event.getSender().compareTo(getName()) > 0) {
                SetTargetLocationMessage message = (SetTargetLocationMessage) event.getMessage();
                int targetX = (int) (getBattleFieldWidth() - message.getX());
                setTargetLocation(targetX, (int) (getBattleFieldHeight() / 2));
            }
        }
    }

    /**
     * This method performs a dodge when necessary, while staying around the preset target location.
     *
     * @author Robin 'Batman' van Alst
     */
    private void doDodge() {
        if (shouldDoDodge && dodgeAroundLocation != null && getDistanceRemaining() == 0) {
            if (isAtTargetLocation()) {
                //Dodge away from a fired projectile (90 degrees offset from the direction to
                // the robot that fired the projectile.
                Vector2d delta = dodgeAroundLocation.subtract(getLocation());
                delta = delta.rotate(90);
                turnToVector(delta.add(getLocation()));
                setBack(150);
            } else {
                //Dodge back to the target location.
                turnToVector(targetLocation);
                setAhead(targetLocation.subtract(getLocation()).vectorLength());
            }
            shouldDoDodge = false;
        }
    }

    /**
     * This method attempts to set a new team target. It does this by broadcasting
     * a message to the rest of the team; however each robot decides for itself what
     * to do with this new target.
     *
     * @param reference The RobotReference representing the target.
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    @Override
    protected void setTeamTarget(RobotReference reference) {
        //We can only broadcast a message when:
        // - we have nu current team target,
        // - the given reference is not a teammate,
        // - and the given reference is not null.
        if (teamTarget == null && reference != null && !reference.isTeammate()) {
            teamTarget = reference;
            try {
                broadcastMessage(new SetTargetMessage(teamTarget));
            } catch (IOException ioe) {
                ; //ignore
            }
        }
    }

    /**
     * This method override does a dodge whenever an enemy has fired.
     * <p>
     * The difference between this method and the code in OnMessageReceived is,
     * that this method is called when this robot sees that an enemy fired,
     * whereas the onMessageReceived code is called when a teammate sees that
     * an enemy fired.
     * </p>
     *
     * @param location the location of the enemy that fired.
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    @Override
    protected void onEnemyFired(Vector2d location) {
        super.onEnemyFired(location);
        shouldDoDodge = true;
        dodgeAroundLocation = location;
    }

    /**
     * This method makes the robot fire at the current team target.
     *
     * @author Robin 'Batman' van Alst, Justin Hoogstraate
     */
    private void fireAtTarget() {
        if (teamTarget != null) {
            double firepower = Math.min(400 / Vector2d.getDistanceTo(teamTarget.getLocation(), getLocation()), 3);
            targetLeading();
            fire(firepower);
        }
    }

    /**
     * This method aims at the current team target with target leading, essentially nullifying the
     * target's own velocity.
     *
     * @author Justin Hoogstraate
     */
    private void targetLeading() {
        Vector2d velocity = teamTarget.getVelocity();
        Vector2d currentLocation = teamTarget.getLocation();
        double predictionX = currentLocation.getX() + 5 * velocity.getX();
        double predictionY = currentLocation.getY() + 5 * velocity.getY();
        Vector2d prediction = new Vector2d(predictionX, predictionY);
        pointGunToVector(prediction);
    }
}
