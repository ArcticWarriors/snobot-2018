package org.snobot.winch;

import org.snobot.PortMappings2018;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class WinchFactory
{
    /**
     * Creates an winch.
     * 
     * @param aUseCan
     *            True to create a CTRE based winch
     * @param aJoystick
     *            The operator joystick
     * @param aLogger
     *            The logger
     * @return The created elevator
     */
    public IWinch createWinch(boolean aUseCan, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        SpeedController winchMotor = new VictorSP(PortMappings2018.sWINCH_PWM_PORT);
        return new SnobotWinch(winchMotor, aJoystick, aLogger);
    }
}
