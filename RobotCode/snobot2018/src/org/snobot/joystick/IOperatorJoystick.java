package org.snobot.joystick;

import org.snobot.lib.modules.IJoystick;

public interface IOperatorJoystick extends IJoystick
{
    double getWinchSpeed();

    double getElevatorSpeed();

    /**
     * Ask joystick if claw open.
     * 
     * @return true if it is open.
     */
    boolean clawOpen();

    /**
     * Returns if the operator wants a preset height.
     * 
     * @return height to go to.
     */
    Double getPresetHeight();

    boolean useLedChooser();
}
