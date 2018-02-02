package org.snobot.drivetrain;

import org.snobot.lib.modules.ISubsystem;

public interface IDriveTrain extends ISubsystem
{
    /**
     * Gets the robot's current right motor distance.
     * 
     * @return right motor distance
     */
    double getRightDistance();

    /**
     * Gets the robot's current left motor distance.
     * 
     * @return left motor distance
     */
    double getLeftDistance();

    /**
     * Sets the robot's current left and right motor speed.
     * 
     * @param aLeftSpeed
     *            - left motor speed
     * @param aRightSpeed
     *            - right motor speed
     */
    void setLeftRightSpeed(double aLeftSpeed, double aRightSpeed);

    /**
     * Gets the robot's current left motor speed.
     * 
     * @return left motor speed
     */
    double getLeftMotorSpeed();

    /**
     * Gets the robot's current right motor speed.
     * 
     * @return right motor distance
     */
    double getRightMotorSpeed();

    /**
     * Resets left and right encoders.
     */
    void resetEncoders();
    
    /**
     * Resets gyro.
     */
    void resetHeading();
    
    /**
     * Gets gyro's current angle.
     * 
     * @return gyro angle
     */
    double getHeading();
}
