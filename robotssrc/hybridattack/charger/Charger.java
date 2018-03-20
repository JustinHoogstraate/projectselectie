package hybridattack.charger;

import robocode.HitRobotEvent;
import hybridattack.Generic.HybridAttackBase;

    public class Charger extends HybridAttackBase {
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
    public void onHitRobot(HitRobotEvent e){
        if(e.getEnergy() > 16){
            fire(3);
        }
        else if(e.getEnergy() > 13) {
            fire(2.5);
        }
        else if(e.getEnergy() > 10) {
            fire(2);
        }
        else if(e.getEnergy() > 7) {
            fire(1.5);
        }
        else if(e.getEnergy() > 4){
            fire(1);
        }
        else if (e.getEnergy() > 2 ){
            fire(.5);
        }
        else if (e.getEnergy() > .6){
            fire(.1);
        }
        ahead(50);
    }


}
