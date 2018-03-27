package hybridattack.targetleader;

import hybridattack.Generic.EnemyFiredMessage;
import hybridattack.Generic.HybridAttackBase;
import hybridattack.Generic.Vector2d;
import robocode.MessageEvent;

public class TargetLeader extends HybridAttackBase {
    private boolean shouldDoDodge = false;
    private Vector2d dodgeAroundLocation = null;

    @Override
    public void run() {
        while (true) {
            doDodge();
            super.run();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);
        if (event.getMessage() instanceof EnemyFiredMessage) {
            EnemyFiredMessage message = (EnemyFiredMessage)event.getMessage();
            dodgeAroundLocation = message.getFiredFromLocation();
            shouldDoDodge = true;
        }
    }

    private void doDodge() {
        if (shouldDoDodge && dodgeAroundLocation != null) {
            double dodgeAngle = getLocation().subtract(dodgeAroundLocation).getWorldBearing();
            double relativeDodgeAngle = dodgeAngle - getHeading();

            if (relativeDodgeAngle > 0) {
                setTurnRight(relativeDodgeAngle);
            }
            else {
                setTurnLeft(Math.abs(relativeDodgeAngle));
            }

            boolean forward = (int)(Math.random() * 100) % 2 == 1;
            if (forward) {
                setAhead(100);
            }
            else {
                setBack(100);
            }

            shouldDoDodge = false;
        }
    }

    @Override
    protected void onEnemyFired(Vector2d location) {
        super.onEnemyFired(location);
        shouldDoDodge = true;
        dodgeAroundLocation = location;
    }
}
