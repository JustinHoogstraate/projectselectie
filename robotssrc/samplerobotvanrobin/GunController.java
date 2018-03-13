package samplerobotvanrobin;

import robocode.TeamRobot;

public class GunController extends RobotController {
    private final boolean DO_TARGET_LEADING = false;
    private final double TARGET_LEADING_FACTOR = 0.0;
    private boolean aimedAtTarget = false;


    public GunController(SMILE owner) {
        super(owner);
    }
    @Override
    public void update() {
        EnemyReference target = owner.getCurrentTarget();
        aimAtTarget(target);
        if (aimedAtTarget && owner.getGunHeat() < 0.7) {
            owner.fire(3);
        }
    }

    private double getAimAngle(EnemyReference target, Vector2d myLocation) {
        Vector2d targetLocation = target.getWorldLocation();
        Vector2d relativeLocation = myLocation.subtract(targetLocation);
        if (DO_TARGET_LEADING) {
            //Vector2d targetLeadingVector = getTargetLeadingVector();
            //relativeLocation = relativeLocation.add(targetLeadingVector);
        }

        double targetBearing = relativeLocation.getWorldBearing();

        return targetBearing;
    }

    private void aimAtTarget(EnemyReference target) {
        if (target != null) {
            double angle = getAimAngle(target, owner.getWorldLocation());
            double bodyHeading = owner.getHeading();
            double gunHeading = owner.getGunHeading();
            double localHeading = angle - gunHeading;
            if (localHeading > 180) {
                localHeading -= 360;
            }
            if (localHeading > 0) {
                owner.setTurnGunRight(localHeading);
            } else if (localHeading < 0) {
                owner.setTurnGunLeft(localHeading * -1);
            }
            if (Math.abs(localHeading) <= 1) {
                aimedAtTarget = true;
            }
        }
    }

    private Vector2d getTargetLeadingVector()
    {
        EnemyReference target = owner.getCurrentTarget();
        Vector2d myWorldLocation = owner.getWorldLocation();
        Vector2d targetWorldLocation = target.getWorldLocation();
        Vector2d delta = targetWorldLocation.subtract(myWorldLocation);
        Vector2d targetVelocity = target.getVelocityVector();

        double distance = delta.vectorLength();
        double multiplier = TARGET_LEADING_FACTOR * (distance / 100.0);

        return targetVelocity.multiply(multiplier);
    }
}
