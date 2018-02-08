package org.snobot.commands;

import java.util.List;

import org.snobot.Properties2018;
import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.lib.InDeadbandHelper;
import org.snobot.lib.Utilities;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToXY extends Command
{
    private final double mX;
    private final double mY;
    private final IDriveTrain mDriveTrain;
    private final IPositioner mPositioner;
    private final InDeadbandHelper mInDeadbandHelper;
    private final double mAllowedError;

    private boolean mFinished;

    /**
     * Drives to a set position smoothly.
     * 
     * @param aDriveTrain
     *            The drivetrain for the robot.
     * @param aPositioner
     *            The robot positioner.
     * @param aX
     *            The x coordinate for the robot.
     * @param aY
     *            The y coordinate for the robot.
     * @param aAllowableError
     *            The distance away the robot must be from the endpoint, in
     *            inches, before the command finishes
     */
    public GoToXY(IDriveTrain aDriveTrain, IPositioner aPositioner, double aX, double aY, double aAllowableError)
    {
        mX = aX;
        mY = aY;
        mAllowedError = aAllowableError;

        mDriveTrain = aDriveTrain;
        mPositioner = aPositioner;
        mInDeadbandHelper = new InDeadbandHelper(3);
    }

    /**
     * Parses the GoToXY command in the command parser map.
     * 
     * @param aArgs
     *            The args passed in from the commandparser.
     * @param aSnobot
     *            The robot.
     * @return Returns the goToXY command.
     */
    public static GoToXY parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double xcoordinate = Double.parseDouble(aArgs.get(1));
        double ycoordinate = Double.parseDouble(aArgs.get(2));
        double allowableError = 5;
        if (aArgs.size() >= 4)
        {
            allowableError = Double.parseDouble(aArgs.get(3));
        }

        IDriveTrain snobot = aSnobot.getDrivetrain();
        IPositioner snobotPositioner = aSnobot.getPositioner();
        return new GoToXY(snobot, snobotPositioner, xcoordinate, ycoordinate, allowableError);
    }

    @Override
    public void execute()
    {
        double xPosition = mPositioner.getXPosition();
        double yPosition = mPositioner.getYPosition();

        double dx = mX - xPosition;
        double dy = mY - yPosition;
        double distanceError = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double angle = Math.toDegrees(Math.atan2(dx, dy));
        double angleError = angle - mPositioner.getOrientationDegrees();

        angleError = Utilities.boundAngleNeg180to180Degrees(angleError);

        double distanceKp = Properties2018.sGO_TO_XY_KPD.getValue();
        double angleKp = Properties2018.sGO_TO_XY_KPA.getValue();
        double leftSpeed = distanceError * distanceKp + angleKp * angleError;
        double rightSpeed = distanceError * distanceKp - angleKp * angleError;
        
        boolean atPlace = Math.abs(distanceError) < mAllowedError;
        mFinished = mInDeadbandHelper.isFinished(atPlace);

        if (mFinished)
        {
            mDriveTrain.stop();
        }
        else
        {
            mDriveTrain.setLeftRightSpeed(leftSpeed, rightSpeed);
        }
        
        SmartDashboard.putNumber("AngleError", angleError);
        SmartDashboard.putNumber("DistanceError", distanceError);
    }

    @Override
    protected boolean isFinished()
    {
        return mFinished;
    }
}
