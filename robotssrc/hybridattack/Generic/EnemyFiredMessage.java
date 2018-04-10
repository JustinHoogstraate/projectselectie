package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to broadcast the event of an enemy firing a bullet.
 *
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class EnemyFiredMessage implements Serializable {
    private Vector2d firedFromLocation;


    /**
     * This constructor is used to create and EnemyFiresMessage.
     *
     * @param firedFromLocation this Vector2D is created when an Enemy fires a bullet.
     * @author Justin Hoogstraate.
     */
    public EnemyFiredMessage(Vector2d firedFromLocation) {
        this.firedFromLocation = firedFromLocation;
    }

    /**
     * this methor returns the location from which was fires.
     *
     * @return the location generated when an enemy has fired.
     * @author Justing Hoogstraate.
     */
    public Vector2d getFiredFromLocation() {
        return firedFromLocation;
    }
}
