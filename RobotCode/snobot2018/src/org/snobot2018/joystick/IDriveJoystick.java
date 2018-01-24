package org.snobot2018.joystick;

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
}
