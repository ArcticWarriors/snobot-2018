package org.snobot.claw;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.leds.ILedManager;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotClaw implements IClaw
{
    private static final DoubleSolenoid.Value sCLAW_OPEN_VALUE = Value.kForward;
    private static final DoubleSolenoid.Value sCLAW_CLOSE_VALUE = Value.kReverse;

    private final DoubleSolenoid mDoubleSolenoid;
    private final ILogger mLogger;
    private final IOperatorJoystick mOperatorJoystick;
    private final ILedManager mLedManager;

    /**
     * This is the Snobot Claw constructor.
     * 
     * @param aDoubleSolenoid
     *            The claw solenoid.
     * @param aLogger
     *            Logs the claw status.
     * @param aOperatorJoystick
     *            the claw joystick.
     */
    public SnobotClaw(DoubleSolenoid aDoubleSolenoid, ILogger aLogger, IOperatorJoystick aOperatorJoystick, ILedManager aLedManager)
    {
        mDoubleSolenoid = aDoubleSolenoid;
        mLogger = aLogger;
        mOperatorJoystick = aOperatorJoystick;
        mLedManager = aLedManager;

    }

    @Override
    public void open()
    {
        mDoubleSolenoid.set(sCLAW_OPEN_VALUE);
    }

    @Override
    public void close()
    {
        mDoubleSolenoid.set(sCLAW_CLOSE_VALUE);
    }

    @Override
    public void control()
    {
        if (mOperatorJoystick.clawOpen())
        {
            open();
        }
        else
        {
            close();
        }
    }

    @Override
    public void stop()
    {
        mDoubleSolenoid.set(Value.kOff);
    }

    @Override
    public void update()
    {
        mLedManager.setClawState(isOpen());
    }


    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("DoubleSolenoid");
    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mDoubleSolenoid.get().toString());
    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putString(SmartDashboardNames.sSNOBOT_CLAW_POSITION, mDoubleSolenoid.get().toString());
    }

    @Override
    public boolean isOpen()
    {
        return mDoubleSolenoid.get() == sCLAW_OPEN_VALUE;
    }

}
