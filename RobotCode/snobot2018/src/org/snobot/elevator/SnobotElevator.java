package org.snobot.elevator;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.InDeadbandHelper;
import org.snobot.lib.logging.ILogger;
import org.snobot.lib.modules.ISubsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotElevator implements IElevator, ISubsystem
{
    private final SpeedController mElevatorMotor;
    private final Encoder mElevatorEncoder;
    private final IOperatorJoystick mJoystick;
    private double mActualHeight;
    private double mJoystickSpeed;
    private final ILogger mLogger;
    private double mTargetHeight;
    private boolean mGotoHeight;
    private final double mKp;
    private final double mHeightDeadband;
    private final double mMaxHeight;
    private final double mMinHeight;
    private final double mDeadband;
    private final InDeadbandHelper mDeadBandHelper;


    /**
     * This is the constructor for the SnobotElevator.
     * 
     * @param aElevatorMotor
     *            is a SpeedController that controls the speed of the elevator
     *            motor.
     * @param aElevatorEncoder
     *            measures the distance the elevator has moved.
     * @param aJoystick
     *            is the operator joystick.
     * @param aLogger
     *            logs the actions of the elevator in the log file.
     */
    public SnobotElevator(SpeedController aElevatorMotor, Encoder aElevatorEncoder, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        mElevatorMotor = aElevatorMotor;
        mElevatorEncoder = aElevatorEncoder;
        mJoystick = aJoystick;
        mLogger = aLogger;
        mActualHeight = 0;
        mJoystickSpeed = 0;
        mGotoHeight = false;
        mKp = Properties2018.sELEVATOR_K_P.getValue();
        mHeightDeadband = Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue();
        mMaxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        mMinHeight = Properties2018.sELEVATOR_MIN_HEIGHT.getValue();
        mDeadband = Properties2018.sELEVATOR_DEADBAND.getValue();
        mDeadBandHelper = new InDeadbandHelper(3);
    }

    /**
     * This calculates the height of the elevator.
     */
    private void caluculateHeight()
    {

        double encoderPosition = mElevatorEncoder.getDistance();
        mActualHeight = encoderPosition;
    }


    @Override
    public void control()
    {

        if (mGotoHeight)
        {
            gotoHeight();
        }
        else
        {
            setMotorSpeed(mJoystickSpeed);
        }
    }

    @Override
    public void stop()
    {
        mGotoHeight = false;
        setMotorSpeed(0);
    }

    @Override
    public void update()
    {
        caluculateHeight();
        mJoystickSpeed = mJoystick.getElevatorSpeed();

        if (-mDeadband < mJoystickSpeed && mJoystickSpeed < mDeadband)
        {
            mGotoHeight = false;
        }
    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("ElevatorHeight");
        mLogger.addHeader("ElevatorSpeed");

    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mActualHeight);
        mLogger.updateLogger(mJoystickSpeed);

    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sELEVATOR_HEIGHT, mActualHeight);
        SmartDashboard.putNumber(SmartDashboardNames.sELEVATOR_MOTOR_SPEED, mJoystickSpeed);

    }

    @Override
    public double getHeight()
    {

        return mActualHeight;
    }

    @Override
    public void setMotorSpeed(double aSpeed)
    {
        if (canMove(aSpeed))
        {
            mElevatorMotor.set(aSpeed);
        }
        else
        {
            stop();
        }
    }

    /**
     * Decides if the elevator can or can't move.
     * 
     * @param aDirection
     *            is the direction the elevator wants to move.
     * @return If true the elevator can move and if false then the elevator
     *         can't move.
     */
    protected boolean canMove(double aDirection)
    {
        boolean canMove = true;

        if (aDirection > 0 && (mMaxHeight <= mActualHeight))
        {   
            canMove = false;
        }
        else if (aDirection < 0 && (mMinHeight >= mActualHeight))
        {
            canMove = false;
        }
        
        return canMove;
    }

    @Override
    public void setHeight(double aHeight)
    {
        mTargetHeight = aHeight;
        mGotoHeight = true;
    }

    @Override
    public boolean gotoHeight()
    {
        double deltaHeight = mTargetHeight - mActualHeight;

        boolean isFinished = -mHeightDeadband < deltaHeight && deltaHeight < mHeightDeadband;
        boolean isAtHeight = mDeadBandHelper.isFinished(isFinished);
        if (isAtHeight)
        {
            stop();
        }
        else
        {
            setMotorSpeed(deltaHeight * mKp);
        }
        return isAtHeight;
    }
    
}
