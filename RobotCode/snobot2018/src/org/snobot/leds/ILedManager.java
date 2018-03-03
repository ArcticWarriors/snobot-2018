package org.snobot.leds;

import org.snobot.autonomous.AutonSelectionType;
import org.snobot.lib.modules.IUpdateableModule;

public interface ILedManager extends IUpdateableModule
{

    /**
     * Asks to see if the claw is open.
     * 
     * @param aOpen
     *            True is open False is closed
     */
    void setClawState(boolean aOpen);

    void setAutoSelection(AutonSelectionType aSelection);

    void setElevatorError(double aActual, double aDesired, boolean aWithinDeadband);

    void setTrajectoryPercentageComplete(double aPercentComplete);
}
