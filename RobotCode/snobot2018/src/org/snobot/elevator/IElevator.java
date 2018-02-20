package org.snobot.elevator;

import org.snobot.lib.modules.ISubsystem;

/**
 * This is the interface for the elevator that allows someone to move the
 * elevator up and down.
 * 
 * @author Josh
 *
 */
public interface IElevator extends ISubsystem
{
    /**
     * Gets the elevator height in inches.
     * 
     * @return the elevator height in inches.
     */
    double getHeight();

    /**
     * Sets the motor speed when a button is pressed.
     * 
     * @param aSpeed
     *            is the MotorSpeed
     */
    void setMotorSpeed(double aSpeed);

    /**
     * Gets the speed (percent output) of the motor.
     * 
     * @return The speed
     */
    double getSpeed();

    /**
     * This calculates the deltaHeight and sets the InDeadbandHelper with 3
     * loops and if the deltaHeight is in between -Deadband and Deadband the
     * program finishes but if it is not then it sets the motor speed using Kp.
     * and the delta Distance.
     * 
     * @param aHeight
     *            Height to go to
     * @return true
     */
    boolean gotoHeight(double aHeight);

    /**
     * Resets the Elevator's encoders.
     */
    void resetEncoders();
}
