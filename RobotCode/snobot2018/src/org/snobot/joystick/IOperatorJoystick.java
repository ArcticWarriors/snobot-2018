package org.snobot.joystick;

import org.snobot.lib.modules.IJoystick;

public interface IOperatorJoystick extends IJoystick
{
    double getElevatorSpeed();

    /**
     * Ask joystick if claw open.
     * 
     * @return true if it is open.
     */
    boolean clawOpen();
}
