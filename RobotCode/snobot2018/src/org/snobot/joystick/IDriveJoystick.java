package org.snobot.joystick;

import org.snobot.lib.modules.IJoystick;

public interface IDriveJoystick extends IJoystick
{

    /**
     * Returns the Right Speed.
     * 
     * @return Returns the Right Speed
     */
    double getRightspeed();

    /**
     * Returns the Left Speed.
     * 
     * @return Returns the Left Speed
     */
    double getLeftspeed();

    /**
     * Indicates if the driver wants the slow modifier placed on the joysticks.
     * 
     * @return True if slow mode
     */
    boolean isSlowMode();

    /**
     * Indicates if the driver wants the super slow modifier placed on the
     * joysticks.
     * 
     * @return True if slow mode
     */
    boolean isSuperSlowMode();
}
