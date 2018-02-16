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

    /**
     * This calculates the deltaHeight and sets the InDeadbandHelper with 3
     * loops and if the deltaHeight is in between -Deadband and Deadband the
     * program finishes but if it is not then it sets the motor speed using Kp.
     * and the delta Distance.
     * 
     * @return True if height reached.
     */
    boolean gotoHeight();
}
