package org.snobot.elevator;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.leds.ILedManager;
import org.snobot.lib.InDeadbandHelper;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class ASnobotElevator<SpeedControllerType extends SpeedController> implements IElevator
{
    protected final ILedManager mLedManager;
    protected final SpeedControllerType mElevatorMotor;
    protected final IOperatorJoystick mJoystick;
    protected final ILogger mLogger;
    
    protected double mActualHeight;
    protected double mJoystickSpeed;
    protected final double mHeightDeadband;
    protected final double mMaxHeight;
    protected final double mMinHeight;
    protected final double mJoystickDeadband;
    protected final InDeadbandHelper mDeadBandHelper;
    protected final ResetElevatorCommand mResetElevatorCommand;


    /**
     * This is the constructor for the SnobotElevator.
     * 
     * @param aElevatorMotor
     *            is a SpeedController that controls the speed of the elevator
     *            motor.
     * @param aJoystick
     *            is the operator joystick.
     * @param aLogger
     *            logs the actions of the elevator in the log file.
     */
    public ASnobotElevator(ILedManager aLedManager, SpeedControllerType aElevatorMotor, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        mLedManager = aLedManager;
        mElevatorMotor = aElevatorMotor;
        mJoystick = aJoystick;
        mLogger = aLogger;
        mActualHeight = 0;
        mJoystickSpeed = 0;
        mHeightDeadband = Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue();
        mMaxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        mMinHeight = Properties2018.sELEVATOR_MIN_HEIGHT.getValue();
        mJoystickDeadband = Properties2018.sELEVATOR_JOYSTICK_DEADBAND.getValue();
        mDeadBandHelper = new InDeadbandHelper(3);

        mResetElevatorCommand = new ResetElevatorCommand(this);
    }


    @Override
    public void control()
    {

        if (mJoystick.getPresetHeight() != null) // NOPMD
        {
            gotoHeight(mJoystick.getPresetHeight());
        }
        else if (mJoystickDeadband < Math.abs(mJoystickSpeed))
        {
            setMotorSpeed(mJoystickSpeed);
        }
        else
        {
            this.stop();
        }

    }

    @Override
    public void stop()
    {
        setMotorSpeed(0);
    }

    @Override
    public void update()
    {
        mActualHeight = caluculateHeight();
        mJoystickSpeed = mJoystick.getElevatorSpeed();
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
        SmartDashboard.putNumber(SmartDashboardNames.sELEVATOR_MOTOR_SPEED, getSpeed());

        SmartDashboard.putData("Reset Encoders", mResetElevatorCommand);
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
        if (Properties2018.sELEVATOR_OVERRIDE_SAFETY.getValue())
        {
            return true;
        }

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

    /**
     * This calculates the height of the elevator.
     */
    protected abstract double caluculateHeight();
}
