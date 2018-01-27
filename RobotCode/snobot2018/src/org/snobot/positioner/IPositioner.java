package org.snobot.positioner;

import org.snobot.lib.modules.ISubsystem;

public interface IPositioner extends ISubsystem
{

    /**
     * Gets the robot's current X-position, in inches.
     * 
     * @return X-position
     */
    public double getXPosition();

    /**
     * Gets the robot's current Y-position, in inches.
     * 
     * @return Y-position.
     */
    public double getYPosition();

    /**
     * Gets the angle of the robot, in degrees.
     * 
     * @return The angle
     */
    public double getOrientationDegrees();
    
    /**
     * Gets the robot's current angle, in radians.
     * 
     * @return The angle
     */
    public double getOrientationRadians();

    /**
     * The total distance traversed by the robot since the last reset.
     * 
     * @return The distance traveled, in inches
     */
    public double getTotalDistance();

    /**
     * Sets the starting position of the robot.
     * 
     * @param aX
     *            The starting X position
     * @param aY
     *            The starting Y Position
     * @param aAngle
     *            The starting Angle (in degrees)
     */
    public void setPosition(double aX, double aY, double aAngle);
}
