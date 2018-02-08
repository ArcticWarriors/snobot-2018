package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.wpilibj.command.Command;

public class StupidGoToXY extends Command
{
    private final double mX;
    private final double mY;
    private final IDriveTrain mDriveTrain;
    private final IPositioner mPositioner;
    private final double mSpeed;
    private boolean mFinished;

    /**
     * Goes to a position.
     * 
     * @param aDriveTrain
     *            The robot drivetrain.
     * @param aPositioner
     *            The robot positioner.
     * @param aX
     *            The x coordinate for the robot.
     * @param aY
     *            The y coordinate for the robot.
     * @param aSpeed
     *            The speed of the robot.
     */
    public StupidGoToXY(IDriveTrain aDriveTrain, IPositioner aPositioner, double aX, double aY, double aSpeed)
    {
        mX = aX;
        mY = aY;
        mDriveTrain = aDriveTrain;
        mPositioner = aPositioner;
        mSpeed = aSpeed;
    }

    /**
     * Adds the command to the command parser.
     * 
     * @param aArgs
     *            This is the list of args passed in from the command file.
     * @param aSnobot
     *            The robot object to access subsystems.
     * @return stupid goToXY command
     */
    public static StupidGoToXY parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double xcoordinate = Double.parseDouble(aArgs.get(1));
        double ycoordinate = Double.parseDouble(aArgs.get(2));
        double speed = Double.parseDouble(aArgs.get(3));
        IDriveTrain snobot = aSnobot.getDrivetrain();
        IPositioner snobotPositioner = aSnobot.getPositioner();
        return new StupidGoToXY(snobot, snobotPositioner, xcoordinate, ycoordinate, speed);
    }

    @Override
    public void execute()
    {
        double dx;
        double dy;
        double xposition = mPositioner.getXPosition();
        double yposition = mPositioner.getYPosition();

        dx = mX - xposition;
        dy = mY - yposition;
        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double angle = Math.atan2(dx, dy);
        double angleError = mPositioner.getOrientationDegrees() - angle;
        if (distance >= 5)
        {

            if (angleError > 0)
            {
                mDriveTrain.setLeftRightSpeed(mSpeed + .1, mSpeed - .1);
            }
            else if (angleError < 0)
            {
                mDriveTrain.setLeftRightSpeed(mSpeed - .1, mSpeed + .1);
            }
            else
            {
                mDriveTrain.setLeftRightSpeed(mSpeed, mSpeed);
            }
        }
        else
        {
            mFinished = true;
        }

    }

    @Override
    public void end()
    {
        mDriveTrain.stop();
    }

    @Override
    protected boolean isFinished()
    {
        return mFinished;
    }
}
