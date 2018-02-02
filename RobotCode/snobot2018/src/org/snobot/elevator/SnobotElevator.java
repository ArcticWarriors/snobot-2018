package org.snobot.elevator;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
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
    private double mHeight;
    private double mElevatorSpeed;
    private final ILogger mLogger;

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
        mHeight = 0;
        mElevatorSpeed = 0;
    }

    /**
     * This calculates the height of the elevator.
     */
    private void caluculateHeight()
    {

        double encoderPosition = mElevatorEncoder.getDistance();
        mHeight = encoderPosition;
    }


    @Override
    public void control()
    {
        setMotorSpeed(mElevatorSpeed);

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
        mElevatorSpeed = mJoystick.getElevatorSpeed();

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
        mLogger.updateLogger(mHeight);
        mLogger.updateLogger(mElevatorSpeed);

    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sELEVATOR_HEIGHT, mHeight);
        SmartDashboard.putNumber(SmartDashboardNames.sELEVATOR_MOTOR_SPEED, mElevatorSpeed);

    }

    @Override
    public double getHeight()
    {

        return mHeight;
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
}
