package hybridattack.Generic;

import java.io.Serializable;

/**
 * This message is used to broadcast the location of a scanned target.
 * @author Justin Hoogstraate, Robin van Alst, Thomas Heinsbroek & Vincent Luiten.
 */

public class SetTargetLocationMessage implements Serializable {
   private Vector2d location;

    /**
     * this constructor is used to create a SetTargetLocationMessage.
     * @param location the location that is used to create the SetTargetLocationMessage.
     * @author Robin van Alst.
     */
    public SetTargetLocationMessage(Vector2d location) {
        this.location = location;
    }

    /**
     * This method gets the location value from the message.
     * @return the location in the message.
     * @author Robin van Alst, Justin Hoogstraate & Thomas Heinsbroek.
     */
    public Vector2d getLocation() {
        return location;
    }

    /**
     * This method sets the location of the message.
     * @param location the location that the location of the message is to be set to.
     * @author Robin van Alst, Justin Hoogstraate & Thomas Heinsbroek.
     */
    public void setLocation(Vector2d location) {
        this.location = location;
    }
}
