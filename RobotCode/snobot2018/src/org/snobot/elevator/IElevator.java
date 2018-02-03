package org.snobot.elevator;

/**
 * This is the interface for the elevator that allows someone to move the
 * elevator up and down.
 * 
 * @author Josh
 *
 */
public interface IElevator
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
     * This sets the height of the elevator.
     * 
     * @param aHeight
     *            is the height that we want to go to in inches.
     * 
     */
    void setHeight(double aHeight);

}
