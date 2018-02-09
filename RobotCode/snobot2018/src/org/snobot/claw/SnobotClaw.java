package org.snobot.claw;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotClaw implements IClaw
{
    private final DoubleSolenoid mDoubleSolenoid;
    private final ILogger mLogger;
    private final IOperatorJoystick mOperatorJoystick;

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
    public SnobotClaw(DoubleSolenoid aDoubleSolenoid, ILogger aLogger, IOperatorJoystick aOperatorJoystick)
    {
        mDoubleSolenoid = aDoubleSolenoid;
        mLogger = aLogger;
        mOperatorJoystick = aOperatorJoystick;
    }

    @Override
    public void open()
    {
        mDoubleSolenoid.set(Value.kForward);
    }

    @Override
    public void close()
    {
        mDoubleSolenoid.set(Value.kReverse);
    }

    @Override
    public void control()
    {
        if (mOperatorJoystick.clawOpen())
        {
            mDoubleSolenoid.set(Value.kForward);
        }
        
        else
        {
            mDoubleSolenoid.set(Value.kReverse);
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
        // No action required.
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

}
