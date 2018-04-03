package hybridattack.targetleader;

import hybridattack.Generic.EnemyFiredMessage;
import hybridattack.Generic.HybridAttackBase;
import hybridattack.Generic.SetTargetMessage;
import hybridattack.Generic.Vector2d;
import robocode.MessageEvent;
import samplerobotvanrobin.RobotReference;

import java.io.IOError;
import java.io.IOException;

public class TargetLeader extends HybridAttackBase {
    private boolean shouldDoDodge = false;
    private Vector2d dodgeAroundLocation = null;

    @Override
    public void run() {
        while (true) {
            doDodge();
            super.run();
            fireAtTarget();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);
        if (event.getMessage() instanceof EnemyFiredMessage) {
            EnemyFiredMessage message = (EnemyFiredMessage) event.getMessage();
            dodgeAroundLocation = message.getFiredFromLocation();
            shouldDoDodge = true;
        }
    }

    private void doDodge() {
        if (shouldDoDodge && dodgeAroundLocation != null) {
            double dodgeAngle = getLocation().subtract(dodgeAroundLocation).getWorldBearing() + 90;
            double relativeDodgeAngle = dodgeAngle - getHeading();
            while (relativeDodgeAngle >= 360) {
                relativeDodgeAngle -= 360;
            }

            if (relativeDodgeAngle > 0) {
                setTurnRight(relativeDodgeAngle);
            } else {
                setTurnLeft(Math.abs(relativeDodgeAngle));
            }

            boolean forward = true;//(int)(Math.random() * 100) % 2 == 1;

            if (forward) {
                setAhead(100);
            } else {
                setBack(100);
            }

            shouldDoDodge = false;
        }
    }

    @Override
    protected void setTeamTarget(hybridattack.Generic.RobotReference reference) {
        if (teamTarget == null && !reference.isTeammate() && teamTarget != reference) {
            teamTarget = reference;
            try {
                broadcastMessage(new SetTargetMessage(teamTarget));
            } catch (IOException ioe) {
            }
        }

    }

    @Override
    protected void onEnemyFired(Vector2d location) {
        super.onEnemyFired(location);
        shouldDoDodge = true;
        dodgeAroundLocation = location;
    }

    private void fireAtTarget() {
        if (teamTarget != null) {
            pointGunToVector(teamTarget.getLocation());
            double firepower = Math.min(400 / Vector2d.getDistanceTo(teamTarget.getLocation(), location), 3);
            fire(firepower);
        }
    }
}
