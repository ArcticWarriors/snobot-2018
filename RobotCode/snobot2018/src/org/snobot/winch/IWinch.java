package org.snobot.winch;

public interface IWinch
{
    /**
     * Sets the winch motor speed.
     * @param aSpeed 
     * the speed to set the motor to.
     */
    void setMotorSpeed(double aSpeed);

    double getWinchSpeed();
}
