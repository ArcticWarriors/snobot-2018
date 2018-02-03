package org.snobot.elevator;

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
    private final double mDeadband;

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
        mKp = 0.1;
        mDeadband = 3;
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
        setMotorSpeed(0);

    }

    @Override
    public void update()
    {
        caluculateHeight();
        mJoystickSpeed = mJoystick.getElevatorSpeed();

        if (mJoystickSpeed != 0)
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
        mElevatorMotor.set(aSpeed);

    }

    @Override
    public void goUp()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void goDown()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHeight(double aHeight)
    {
        mTargetHeight = aHeight;
        mGotoHeight = true;
    }

    /**
     * This calculates the deltaHeight and sets the InDeadbandHelper with 3
     * loops and if the deltaHeight is in between -Deadband and Deadband the
     * program finishes but if it is not then it sets the motor speed using Kp.
     * and the delta Distance
     */
    void gotoHeight()
    {
        double deltaHeight = mTargetHeight - mActualHeight;
        InDeadbandHelper deadBandHelper = new InDeadbandHelper(3);

        boolean isFinished = -1 * mDeadband < deltaHeight && deltaHeight < mDeadband;

        if (deadBandHelper.isFinished(isFinished))
        {
            mGotoHeight = false;
            setMotorSpeed(0);
        }
        else
        {
            setMotorSpeed(deltaHeight * mKp);
        }
    }
    
}
